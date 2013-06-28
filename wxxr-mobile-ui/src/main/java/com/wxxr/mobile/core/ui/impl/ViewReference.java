/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.DataChangedEvent;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IUIManagementContext;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewReference;
import com.wxxr.mobile.core.ui.api.UIError;

/**
 * @author neillin
 *
 */
public class ViewReference implements IViewReference {
	private final IView referedView;
	private int refCount = 0;
	
	public ViewReference(IView ref){
		this.referedView = ref;
	}

	/**
	 * @param clazz
	 * @return
	 * @see com.wxxr.mobile.core.microkernel.api.IAdaptable#getAdaptor(java.lang.Class)
	 */
	public <T> T getAdaptor(Class<T> clazz) {
		return referedView.getAdaptor(clazz);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IView#getName()
	 */
	public String getName() {
		return referedView.getName();
	}

	/**
	 * @param name
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IView#getDataField(java.lang.String)
	 */
	public IDataField getDataField(String name) {
		return referedView.getDataField(name);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChildrenCount()
	 */
	public int getChildrenCount() {
		return referedView.getChildrenCount();
	}

	/**
	 * @param idx
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(int)
	 */
	public IUIComponent getChild(int idx) {
		return referedView.getChild(idx);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IView#getDataFields()
	 */
	public List<IDataField> getDataFields() {
		return referedView.getDataFields();
	}

	/**
	 * @param key
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#getAttribute(com.wxxr.mobile.core.ui.api.AttributeKey)
	 */
	public <T> T getAttribute(AttributeKey<T> key) {
		return referedView.getAttribute(key);
	}

	/**
	 * @param event
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#notifyDataChanged(com.wxxr.mobile.core.ui.api.DataChangedEvent)
	 */
	public void notifyDataChanged(DataChangedEvent event) {
		referedView.notifyDataChanged(event);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IView#isActive()
	 */
	public boolean isActive() {
		return referedView.isActive();
	}

	/**
	 * 
	 * @see com.wxxr.mobile.core.ui.api.IView#activate()
	 */
	public void activate() {
		referedView.activate();
	}

	/**
	 * @param key
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#hasAttribute(com.wxxr.mobile.core.ui.api.AttributeKey)
	 */
	public boolean hasAttribute(AttributeKey<?> key) {
		return referedView.hasAttribute(key);
	}

	/**
	 * 
	 * @see com.wxxr.mobile.core.ui.api.IView#deactivate()
	 */
	public void deactivate() {
		referedView.deactivate();
	}

	/**
	 * @param name
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(java.lang.String)
	 */
	public IUIComponent getChild(String name) {
		return referedView.getChild(name);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IView#getErrors()
	 */
	public List<UIError> getErrors() {
		return referedView.getErrors();
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#getAttributeKeys()
	 */
	public Set<AttributeKey<?>> getAttributeKeys() {
		return referedView.getAttributeKeys();
	}

	/**
	 * @param clazz
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChildren(java.lang.Class)
	 */
	public <T> List<T> getChildren(Class<T> clazz) {
		return referedView.getChildren(clazz);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#getParent()
	 */
	public IUIContainer getParent() {
		return referedView.getParent();
	}

	/**
	 * @param name
	 * @param clazz
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(java.lang.String, java.lang.Class)
	 */
	public <T> T getChild(String name, Class<T> clazz) {
		return referedView.getChild(name, clazz);
	}

	/**
	 * @param component
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#isSubsidiaryOf(com.wxxr.mobile.core.ui.api.IUIComponent)
	 */
	public boolean isSubsidiaryOf(IUIComponent component) {
		return referedView.isSubsidiaryOf(component);
	}

	/**
	 * 
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#init()
	 */
	public void init(IUIManagementContext ctx) {
		if(this.refCount == 0){
			referedView.init(ctx);
		}
		this.refCount++;
	}

	/**
	 * 
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#destroy()
	 */
	public void destroy() {
		if(this.refCount > 0){
			this.refCount--;
		}
		if(this.refCount == 0){
			referedView.destroy();
		}
	}

	/**
	 * @return
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<IUIComponent> iterator() {
		return referedView.iterator();
	}

	public IView getReferedView() {
		return this.referedView;
	}

	public boolean isInitialized() {
		return this.referedView.isInitialized();
	}
	
	

}
