/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

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
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.IAdaptable#getAdaptor(java.lang.Class)
	 */
	public <T> T getAdaptor(Class<T> clazz) {
		if(clazz == IUIContainer.class){
			return clazz.cast(this);
		}
		return super.getAdaptor(clazz);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<IUIComponent> iterator() {
		return this.container.iterator();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChildrenCount()
	 */
	public int getChildrenCount() {
		return this.container.getChildrenCount();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(int)
	 */
	@SuppressWarnings("unchecked")
	public C getChild(int idx) {
		return (C)this.container.getChild(idx);
	}

	
	protected UIContainer<C> add(IUIComponent child){
		this.container.add(child);
		return this;
	}
	
	protected UIContainer<C> remove(IUIComponent child){
		this.container.remove(child);
		return this;
	}
	 
	@SuppressWarnings("unchecked")
	public C getChild(String name){
		return (C)this.container.getChild(name);
	}


	
	protected UIContainer<C> addFirst(IUIComponent child){
		this.container.addFirst(child);
		return this;
	}

	protected UIContainer<C> addLast(IUIComponent child){
		this.container.addLast(child);
		return this;
	}

	public <T extends C> List<T> getChildren(Class<T> clazz) {
		return this.container.getChildren(clazz);
	}

	public <T extends C> T getChild(String name, Class<T> clazz) {
		return this.container.getChild(name, clazz);
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
		this.container = new GenericContainer<IUIComponent>() {
			
			@Override
			protected void handleRemoved(IUIComponent child) {
				((UIComponent)child).setParent(null);
			}
			
			@Override
			protected void handleDestroy(IUIComponent ui) {
				ui.destroy();
			}
			
			@Override
			protected void handleAdded(IUIComponent child) {
				((UIComponent)child).setParent(UIContainer.this);
			}
			
			@Override
			protected String getObjectId(IUIComponent child) {
				return child.getName();
			}
		};
		super.init(ctx);
	}

	public String[] getChildIds() {
		List<String> list = this.container.getChildrenNames();
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
		this.container.remove(name);
		return this;
	}

}
