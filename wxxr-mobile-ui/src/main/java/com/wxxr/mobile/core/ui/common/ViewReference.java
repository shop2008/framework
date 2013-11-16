/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingValueChangedCallback;
import com.wxxr.mobile.core.ui.api.ISelectionProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IViewReference;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValidationError;


/**
 * @author neillin
 *
 */
public class ViewReference implements IViewReference {
	private final IViewDescriptor descriptor;
	private final IWorkbenchRTContext context;
	private int refCount = 0;
	private IView delegate;
	
	public ViewReference(IWorkbenchRTContext ctx,IViewDescriptor ref){
		this.descriptor = ref;
		this.context = ctx;
	}

	private IView getDelegate() {
		if(this.delegate == null){
			this.delegate = this.descriptor.createPresentationModel(context);
		}
		return this.delegate;
	}
	
	/**
	 * @param clazz
	 * @return
	 * @see com.wxxr.mobile.core.microkernel.api.IAdaptable#getAdaptor(java.lang.Class)
	 */
	public <T> T getAdaptor(Class<T> clazz) {
		return getDelegate().getAdaptor(clazz);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IView#getName()
	 */
	public String getName() {
		return descriptor.getViewId();
	}


	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChildrenCount()
	 */
	public int getChildrenCount() {
		return getDelegate().getChildrenCount();
	}

	/**
	 * @param idx
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(int)
	 */
	public IUIComponent getChild(int idx) {
		return getDelegate().getChild(idx);
	}

	/**
	 * @param key
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#getAttribute(com.wxxr.mobile.core.ui.api.AttributeKey)
	 */
	public <T> T getAttribute(AttributeKey<T> key) {
		return getDelegate().getAttribute(key);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IView#isActive()
	 */
	public boolean isActive() {
		return getDelegate().isActive();
	}

	/**
	 * 
	 * @see com.wxxr.mobile.core.ui.api.IView#activate()
	 */
	public void show() {
		getDelegate().show();
	}

	/**
	 * @param key
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#hasAttribute(com.wxxr.mobile.core.ui.api.AttributeKey)
	 */
	public boolean hasAttribute(AttributeKey<?> key) {
		return getDelegate().hasAttribute(key);
	}

	/**
	 * 
	 * @see com.wxxr.mobile.core.ui.api.IView#deactivate()
	 */
	public void hide() {
		getDelegate().hide();
	}

	/**
	 * @param name
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(java.lang.String)
	 */
	public IUIComponent getChild(String name) {
		return getDelegate().getChild(name);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IView#getErrors()
	 */
	public List<ValidationError> getErrors() {
		return getDelegate().getErrors();
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#getAttributeKeys()
	 */
	public Set<AttributeKey<?>> getAttributeKeys() {
		return getDelegate().getAttributeKeys();
	}

	/**
	 * @param clazz
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChildren(java.lang.Class)
	 */
	public <T extends IUIComponent> List<T> getChildren(Class<T> clazz) {
		return getDelegate().getChildren(clazz);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#getParent()
	 */
	public IUIContainer<?> getParent() {
		return getDelegate().getParent();
	}

	/**
	 * @param name
	 * @param clazz
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(java.lang.String, java.lang.Class)
	 */
	public <T extends IUIComponent> T getChild(String name, Class<T> clazz) {
		return getDelegate().getChild(name, clazz);
	}

	/**
	 * @param component
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#isSubsidiaryOf(com.wxxr.mobile.core.ui.api.IUIComponent)
	 */
	public boolean isSubsidiaryOf(IUIComponent component) {
		return getDelegate().isSubsidiaryOf(component);
	}

	/**
	 * 
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#init()
	 */
	public void init(IWorkbenchRTContext ctx) {
		if(this.refCount == 0){
			getDelegate().init(ctx);
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
			getDelegate().destroy();
		}
	}

	/**
	 * @return
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<IUIComponent> iterator() {
		return getDelegate().iterator();
	}

	public IView getReferedView() {
		return getDelegate();
	}

	public boolean isInitialized() {
		return this.getDelegate().isInitialized();
	}


	/**
	 * @param binding
	 * @see com.wxxr.mobile.core.ui.api.IBindable#doBinding(com.wxxr.mobile.core.ui.api.IBinding)
	 */
	public void doBinding(IBinding<IView> binding) {
		getDelegate().doBinding(binding);
	}

	/**
	 * @param binding
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IBindable#doUnbinding(com.wxxr.mobile.core.ui.api.IBinding)
	 */
	public boolean doUnbinding(IBinding<IView> binding) {
		return getDelegate().doUnbinding(binding);
	}

	public String[] getChildIds() {
		return getDelegate().getChildIds();
	}

	public IUIContainer<IUIComponent> addChild(IUIComponent child) {
		return getDelegate().addChild(child);
	}

	public IUIContainer<IUIComponent> removeChild(IUIComponent child) {
		return getDelegate().removeChild(child);
	}

	public IUIContainer<IUIComponent> removeChild(String name) {
		return getDelegate().removeChild(name);
	}

	public void invokeCommand(String cmdName, InputEvent event) {
		getDelegate().invokeCommand(cmdName, event);
	}

	@Override
	public IBinding<IView> getBinding() {
		return getDelegate().getBinding();
	}

	/**
	 * @param backable
	 * @see com.wxxr.mobile.core.ui.api.IView#show(boolean)
	 */
	public void show(boolean backable) {
		delegate.show(backable);
	}

	/**
	 * @param key
	 * @param val
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUIComponent#setAttribute(com.wxxr.mobile.core.ui.api.AttributeKey, java.lang.Object)
	 */
	public <T> IUIComponent setAttribute(AttributeKey<T> key, T val) {
		 delegate.setAttribute(key, val);
		 return this;
	}

	@Override
	public void setValueChangedCallback(IBindingValueChangedCallback cb) {
		
	}

	@Override
	public ISelectionProvider getSelectionProvider() {
		return delegate.getSelectionProvider();
	}	
}
