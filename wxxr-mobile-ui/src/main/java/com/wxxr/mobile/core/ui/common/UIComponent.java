/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.wxxr.mobile.core.bean.api.ICollectionDecorator;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IBindingValueChangedCallback;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.util.ObjectUtils;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public abstract class UIComponent implements IUIComponent {
	private static final Trace log = Trace.register(UIComponent.class);
	
	private static final ThreadLocal<Boolean> eventDisabled = new ThreadLocal<Boolean>();
	
	public static boolean disableEvents() {
		Boolean val = eventDisabled.get();
		if((val == null)||(val.booleanValue() == false)){
			eventDisabled.set(true);
			return true;
		}
		return false;
	}
	
	public static void enableEvents() {
		eventDisabled.set(null);
	}
	
	public boolean isEventDisabled() {
		return (ModelUtils.isViewOnShow(this) == false)||((eventDisabled.get() != null)&&(eventDisabled.get().booleanValue()));
	}
	
	private String name;
	private Map<AttributeKey<?>, Object> attrs;
	private IUIContainer<IUIComponent> parent;
	private IWorkbenchRTContext ctx;
	private IBindingValueChangedCallback callback;
	
	public UIComponent(){
	}
	
	public UIComponent(String name){
		this.name = name;
	}
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
		return this.attrs != null ? attrs.containsKey(key) : false;
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
		}else if((old instanceof ICollectionDecorator)&&((ICollectionDecorator)old).checkChangedNClear()){
			fireDataChangedEvent(key);
		}
		return this;
	}
		
	
	@SuppressWarnings("unchecked")
	public <T> T removeAttribute(AttributeKey<T> key){
		if(!hasAttribute(key)){
			return null;
		}
		T val = this.attrs != null ? (T)this.attrs.remove(key) : null;
		fireDataChangedEvent(key);
		return val;
	}
	
	protected void fireDataChangedEvent(AttributeKey<?> ... keys){
		if(isEventDisabled()){
			return;
		}
		if(log.isDebugEnabled()){
			log.debug("Going to fire ComponentValueChangedEvent for :"+this.toString()+", key :"+StringUtils.join(keys,',')+", callback = "+this.callback);
		}
		if(this.callback != null){
			this.callback.valueChanged(this, keys);
		}
		fireDataChangedEvent(new ComponentValueChangedEventImpl(this, keys));
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
		if(this.parent != null){
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

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#setValueChangedCallback(com.wxxr.mobile.core.ui.api.IBindingValueChangedCallback)
	 */
	@Override
	public void setValueChangedCallback(IBindingValueChangedCallback cb) {
		this.callback = cb;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName()+" [name=" + name + "]";
	}
		
}
