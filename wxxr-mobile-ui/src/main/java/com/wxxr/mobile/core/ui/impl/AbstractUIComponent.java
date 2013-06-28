/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IUIManagementContext;
import com.wxxr.mobile.core.util.ObjectUtils;

/**
 * @author neillin
 *
 */
public abstract class AbstractUIComponent implements IUIComponent {
	
	private String name;
	private Map<AttributeKey<?>, Object> attrs;
	private IUIContainer parent;
	private IUIManagementContext ctx;
	
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
		Object val = this.attrs == null ? attrs.get(key) : null;
		return val != null ? key.getValueType().cast(val) : null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#hasAttribute(com.wxxr.mobile.core.ui.api.AttributeKey)
	 */
	public boolean hasAttribute(AttributeKey<?> key) {
		return this.attrs == null ? attrs.containsKey(key) : false;
	}

	public Set<AttributeKey<?>> getAttributeKeys() {
		return this.attrs == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(this.attrs.keySet());
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#getParent()
	 */
	public IUIContainer getParent() {
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
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * @param parent the parent to set
	 */
	protected void setParent(IUIContainer parent) {
		this.parent = parent;
	}

	protected <T>  void setAttribute(AttributeKey<T> key, T val){
		if(this.attrs == null){
			this.attrs = new HashMap<AttributeKey<?>, Object>();
		}
		T old = getAttribute(key);
		if(!ObjectUtils.isEquals(old, val)){
			this.attrs.put(key, val);
			fireDataChangedEvent(key);
		}
	}
		
	protected void fireDataChangedEvent(AttributeKey<?> ... keys){
		if(getParent() != null){
			getParent().notifyDataChanged(new DataChangedEventImpl(this, keys));
		}
	}
	
	protected IUIManagementContext getUIContext() {
		return this.ctx;
	}
	
	public void init(IUIManagementContext ctx){
		this.ctx = ctx;
	}
	
	public void destroy() {
		if(this.attrs != null){
			this.attrs.clear();
			this.attrs = null;
		}
		if(this.parent == null){
			((AbstractUIContainer)this.parent).remove(this);
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
		
}
