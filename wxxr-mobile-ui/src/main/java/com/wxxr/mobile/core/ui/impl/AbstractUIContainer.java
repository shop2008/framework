/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import java.util.Iterator;
import java.util.List;

import com.wxxr.mobile.core.ui.api.DataChangedEvent;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIContainer;
import com.wxxr.mobile.core.ui.api.IUIManagementContext;

/**
 * @author neillin
 *
 */
public class AbstractUIContainer extends AbstractUIComponent implements IUIContainer {

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
	public AbstractUIComponent getChild(int idx) {
		return (AbstractUIComponent)this.container.getChild(idx);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#notifyDataChanged(com.wxxr.mobile.core.ui.api.DataChangedEvent)
	 */
	public void notifyDataChanged(DataChangedEvent event) {
		IUIContainer p = getParent();
		if(p != null){
			p.notifyDataChanged(event);
		}
	}
	
	protected void add(IUIComponent child){
		this.container.add(child);
	}
	
	protected boolean remove(IUIComponent child){
		return this.container.remove(child);
	}
	 
	public IUIComponent getChild(String name){
		return this.container.getChild(name);
	}


	
	protected void addFirst(IUIComponent child){
		this.container.addFirst(child);
	}

	protected void addLast(IUIComponent child){
		this.container.addLast(child);
	}

	public <T> List<T> getChildren(Class<T> clazz) {
		return this.container.getChildren(clazz);
	}

	public <T> T getChild(String name, Class<T> clazz) {
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
	 * @see com.wxxr.mobile.core.ui.impl.AbstractUIComponent#init(com.wxxr.mobile.core.ui.api.IUIManagementContext)
	 */
	@Override
	public void init(IUIManagementContext ctx) {
		this.container = new GenericContainer<IUIComponent>() {
			
			@Override
			protected void handleRemoved(IUIComponent child) {
				((AbstractUIComponent)child).setParent(null);
			}
			
			@Override
			protected void handleDestroy(IUIComponent ui) {
				ui.destroy();
			}
			
			@Override
			protected void handleAdded(IUIComponent child) {
				((AbstractUIComponent)child).setParent(AbstractUIContainer.this);
			}
			
			@Override
			protected String getObjectId(IUIComponent child) {
				return child.getName();
			}
		};
		super.init(ctx);
	}

}
