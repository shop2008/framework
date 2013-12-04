/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;


/**
 * @author neillin
 *
 */
public class UIContainer<C extends IUIComponent> extends UIComponent implements IUIContainer<C> {

	private GenericContainer<IUIComponent> container;
	
	public UIContainer() {
		super();
	}

	public UIContainer(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IAdaptable#getAdaptor(java.lang.Class)
	 */
	public <T> T getAdaptor(Class<T> clazz) {
		if(clazz == IUIContainer.class){
			return clazz.cast(this);
		}
		if(clazz.isAssignableFrom(getClass())){
			return clazz.cast(this);
		}
		return super.getAdaptor(clazz);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@SuppressWarnings("unchecked")
	public Iterator<IUIComponent> iterator() {
		return this.container != null ? this.container.iterator() : Collections.EMPTY_LIST.iterator();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChildrenCount()
	 */
	public int getChildrenCount() {
		return this.container != null ? this.container.getChildrenCount() : 0;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(int)
	 */
	@SuppressWarnings("unchecked")
	public C getChild(int idx) {
		return this.container != null ? (C)this.container.getChild(idx) : null;
	}

	
	protected UIContainer<C> add(IUIComponent child){
		createContainerIfNotExisting();
		this.container.add(child);
		if((getUIContext() != null)&&(child.isInitialized() == false)){
			child.init(getUIContext());
		}
		return this;
	}

	/**
	 * 
	 */
	protected void createContainerIfNotExisting() {
		if(this.container == null){
			this.container = new GenericContainer<IUIComponent>() {
				
				@Override
				protected void handleRemoved(IUIComponent child) {
					((UIComponent)child).setParent(null);
				}
				
				@Override
				protected void handleDestroy(IUIComponent ui) {
					ui.destroy();
				}
				
				@SuppressWarnings("unchecked")
				@Override
				protected void handleAdded(IUIComponent child) {
					((UIComponent)child).setParent((IUIContainer<IUIComponent>)UIContainer.this);
				}
				
				@Override
				protected String getObjectId(IUIComponent child) {
					return child.getName();
				}

				@Override
				protected void handleInit(IUIComponent ui,
						IWorkbenchRTContext ctx) {
					ui.init(ctx);
				}
			};
		}
	}
	
	protected UIContainer<C> remove(IUIComponent child){
		if(this.container != null){
			this.container.remove(child);
		}
		return this;
	}
	 
	@SuppressWarnings("unchecked")
	public C getChild(String name){
		return this.container != null ? (C)this.container.getChild(name) : null;
	}


	
	protected UIContainer<C> addFirst(IUIComponent child){
		createContainerIfNotExisting();
		this.container.addFirst(child);
		if(getUIContext() != null){
			child.init(getUIContext());
		}
		return this;
	}

	protected UIContainer<C> addLast(IUIComponent child){
		createContainerIfNotExisting();
		this.container.addLast(child);
		if(getUIContext() != null){
			child.init(getUIContext());
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends C> List<T> getChildren(Class<T> clazz) {
		return this.container != null ? this.container.getChildren(clazz) : Collections.EMPTY_LIST;
	}

	public <T extends C> T getChild(String name, Class<T> clazz) {
		return this.container != null ? this.container.getChild(name, clazz) : null;
	}
	
	public void destroy(){
		//destroy children
		if(this.container != null){
			this.container.destroy();
			this.container = null;
		}
		//destroy self
		super.destroy();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.impl.AbstractUIComponent#init(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	@Override
	public void init(IWorkbenchRTContext ctx) {
		super.init(ctx);
		if(this.container != null){
			this.container.init(ctx);
		}
	}

	public String[] getChildIds() {
		List<String> list = this.container != null ? this.container.getChildrenNames() : null;
		return list != null && list.size() > 0 ? list.toArray(new String[list.size()]) : null;
	}

	public IUIContainer<C> addChild(IUIComponent child) {
		add(child);
		return this;
	}

	public IUIContainer<C> removeChild(IUIComponent child) {
		remove(child);
		return this;
	}

	public IUIContainer<C> removeChild(String name) {
		if(this.container != null)
			this.container.remove(name);
		return this;
	}

}
