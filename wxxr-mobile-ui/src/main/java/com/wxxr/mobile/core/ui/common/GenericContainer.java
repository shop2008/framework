/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author neillin
 *
 */
public abstract class GenericContainer<T>  implements Iterable<T>{

	private LinkedList<T> children;
	private Map<String, T> map;
	

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private int idx = 0;
			
			public boolean hasNext() {
				return children != null && idx < children.size() ? true : false;
			}

			public T next() {
				return children.get(idx++);
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChildrenCount()
	 */
	public int getChildrenCount() {
		return children != null ? children.size() : 0;
	}

	@SuppressWarnings("unchecked")
	public List<String> getChildrenNames() {
		return this.map == null||this.map.isEmpty() ? Collections.EMPTY_LIST : new ArrayList<String>(this.map.keySet());
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IUIContainer#getChild(int)
	 */
	public T getChild(int idx) {
		return children != null && idx < children.size() ? children.get(idx) : null;
	}

	
	public void add(T child){
		addChild(child);
		this.children.add(child);
	}
	
	public boolean remove(T child){
		boolean val  = this.children.remove(child);
		this.map.remove(getObjectId(child));
		if(val){
			handleRemoved(child);
		}
		return val;
	}

	public boolean remove(String id){
		T obj = this.map.remove(id);
		if(obj != null){
			this.children.remove(obj);
			return true;
		}
		return false;
	}

	/**
	 * @param child
	 */
	protected abstract void handleRemoved(T child);
//	{
//		child.setParent(null);
//	}
	 
	public T getChild(String name){
		return this.map != null ? this.map.get(name) : null;
	}

	/**
	 * @param child
	 */
	protected void addChild(T child) {
		if(this.children == null){
			this.children = new LinkedList<T>();
			this.map = new HashMap<String,T>();
		}
		this.map.put(getObjectId(child), child);
		handleAdded(child);
	}

	/**
	 * @param child
	 */
	protected abstract void handleAdded(T child);
//	{
//		child.setParent(this);
//	}

	/**
	 * @param child
	 */
	protected abstract String getObjectId(T child);
//	{
//		child.getName();
//	}

	
	public void addFirst(T child){
		addChild(child);
		this.children.addFirst(child);
	}

	public void addLast(T child){
		addChild(child);
		this.children.addLast(child);
	}

	@SuppressWarnings("unchecked")
	public <C> List<C> getChildren(Class<C> clazz) {
		if((this.children == null)||this.children.isEmpty()){
			return Collections.EMPTY_LIST;
		}
		List<C> result = null;
		for (Object ui : this.children) {
			if(clazz.isAssignableFrom(ui.getClass())){
				if(result == null){
					result = new LinkedList<C>();
				}
				result.add(clazz.cast(ui));
			}
		}
		return result == null ? Collections.EMPTY_LIST : result;
	}

	public <C> C getChild(String name, Class<C> clazz) {
		Object ui = getChild(name);
		return ui != null && clazz.isAssignableFrom(ui.getClass()) ? clazz.cast(ui) : null;
	}
	
	public void destroy(){
		//destroy children
		if(this.children != null){
			for (T ui : this.children) {
				handleDestroy(ui);
			}
			this.children.clear();
			this.children = null;
		}
		if(this.map != null){
			this.map.clear();
			this.map = null;
		}
	}

	/**
	 * @param ui
	 */
	protected abstract void handleDestroy(T ui);
//	{
//		ui.destroy();
//	}

}
