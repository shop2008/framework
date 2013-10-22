/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.util.ObjectUtils;

/**
 * @author neillin
 *
 */
public abstract class UIComponent implements IUIComponent {
	
	private String name;
	private Map<AttributeKey<?>, Object> attrs;
	private IUIContainer<IUIComponent> parent;
	private IWorkbenchRTContext ctx;
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IAdaptable#getAdaptor(java.lang.Class)
	 */
	public <T> T getAdaptor(Class<T> clazz) {
		if(clazz == IUIComponent.class){
			return clazz.cast(this);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#getName()
	 */
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#getAttribute(com.wxxr.mobile.core.ui.api.AttributeKey)
	 */
	public <T> T getAttribute(AttributeKey<T> key) {
		Object val = this.attrs != null ? attrs.get(key) : null;
		return val != null ? key.getValueType().cast(val) : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#hasAttribute(com.wxxr.mobile.core.ui.api.AttributeKey)
	 */
	public boolean hasAttribute(AttributeKey<?> key) {
		return this.attrs == null ? attrs.containsKey(key) : false;
	}

	@SuppressWarnings("unchecked")
	public Set<AttributeKey<?>> getAttributeKeys() {
		return this.attrs == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.attrs.keySet());
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#getParent()
	 */
	public IUIContainer<IUIComponent> getParent() {
		return this.parent;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#isSubsidiaryOf(com.wxxr.mobile.core.ui.api.IUIComponent)
	 */
	public boolean isSubsidiaryOf(IUIComponent component) {
		for(IUIComponent p = getParent(); p != null ; p = p.getParent()){
			if(p == component){
				return true;
			}
		}
		return false;
	}

	/**
	 * @param name the name to set
	 */
	public UIComponent setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @param parent the parent to set
	 */
	public UIComponent setParent(IUIContainer<IUIComponent> parent) {
		this.parent = parent;
		return this;
	}

	public <T>  UIComponent setAttribute(AttributeKey<T> key, T val){
		if(this.attrs == null){
			this.attrs = new HashMap<AttributeKey<?>, Object>();
		}
		T old = getAttribute(key);
		if(!ObjectUtils.isEquals(old, val)){
			this.attrs.put(key, val);
			fireDataChangedEvent(key);
		}
		return this;
	}
		
	protected void fireDataChangedEvent(AttributeKey<?> ... keys){
		fireDataChangedEvent(new ValueChangedEventImpl(this, keys));
	}
	
	protected void fireDataChangedEvent(ValueChangedEvent evt){
		if(this.getParent() != null){
			((UIComponent)this.getParent()).fireDataChangedEvent(evt);
		}
	}

	
	protected IWorkbenchRTContext getUIContext() {
		return this.ctx;
	}
	
	public void init(IWorkbenchRTContext ctx){
		this.ctx = ctx;
	}
	
	public void destroy() {
		if(this.attrs != null){
			this.attrs.clear();
			this.attrs = null;
		}
		if(this.parent == null){
			((UIContainer<?>)this.parent).remove(this);
			this.parent = null;
		}
		this.ctx = null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#isInitialized()
	 */
	public boolean isInitialized() {
		return this.ctx != null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#invokeCommand(java.lang.String, java.lang.Object[])
	 */
	public void invokeCommand(String cmdName, InputEvent event) {
		if(getParent() != null){
			getParent().invokeCommand(cmdName, event);
		}
	}
		
}
