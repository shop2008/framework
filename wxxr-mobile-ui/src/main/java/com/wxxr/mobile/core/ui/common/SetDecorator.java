/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;

import com.wxxr.mobile.core.bean.api.ICollectionDecorator;

/**
 * @author neillin
 *
 */
public class SetDecorator<E> implements List<E>,ICollectionDecorator {
	
	private List<E> data;
	final private BindableBeanSupport support;
	final private String name;
	private AtomicBoolean changed = new AtomicBoolean();
	
	public SetDecorator(String propertyName,BindableBeanSupport p){
		this.support = p;
		this.name = propertyName;
	}

	public SetDecorator(String propertyName,BindableBeanSupport p,List<E> list){
		this.support = p;
		this.name = propertyName;
		this.data = list;
	}

	/**
	 * @return the data
	 */
	public List<E> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<E> data) {
		this.data = data;
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(E object) {
		 data.add(object);
		 this.support.firePropertyChange(name, null, object);
		 this.changed.set(true);
		 return true;
	}

	/**
	 * @param location
	 * @param object
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int location, E object) {
		data.add(location, object);
		this.support.firePropertyChange(name, null, object);
		this.changed.set(true);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends E> arg0) {
		 data.addAll(arg0);
		 this.support.firePropertyChange(name, null, this);
		 this.changed.set(true);
		 return true;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int arg0, Collection<? extends E> arg1) {
		 data.addAll(arg0, arg1);
		 this.support.firePropertyChange(name, null, this);
		 this.changed.set(true);
		 return true;
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		data.clear();
		 this.support.firePropertyChange(name, this, null);
		 this.changed.set(true);
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object object) {
		return data.contains(object);
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> arg0) {
		return data.containsAll(arg0);
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.List#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		return data.equals(object);
	}

	/**
	 * @param location
	 * @return
	 * @see java.util.List#get(int)
	 */
	public E get(int location) {
		return data.get(location);
	}

	/**
	 * @return
	 * @see java.util.List#hashCode()
	 */
	public int hashCode() {
		return data.hashCode();
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object object) {
		return data.indexOf(object);
	}

	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator<E> iterator() {
		return data.iterator();
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object object) {
		return data.lastIndexOf(object);
	}

	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	public ListIterator<E> listIterator() {
		return data.listIterator();
	}

	/**
	 * @param location
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator<E> listIterator(int location) {
		return data.listIterator(location);
	}

	/**
	 * @param location
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public E remove(int location) {
		E val = data.remove(location);
		 this.support.firePropertyChange(name, this, null);
		 this.changed.set(true);
		return val;
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object object) {
		boolean val = data.remove(object);
		 this.support.firePropertyChange(name, this, null);
		 this.changed.set(true);
		return val;
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> arg0) {
		boolean val = data.removeAll(arg0);
		this.support.firePropertyChange(name, this, null);
		this.changed.set(true);
		return val;
	}

	/**
	 * @param arg0
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> arg0) {
		return data.retainAll(arg0);
	}

	/**
	 * @param location
	 * @param object
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public E set(int location, E object) {
		E val = data.set(location, object);
		 this.support.firePropertyChange(name, val, object);
		 this.changed.set(true);
		return val;
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return data.size();
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	public List<E> subList(int start, int end) {
		return data.subList(start, end);
	}

	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {
		return data.toArray();
	}

	/**
	 * @param array
	 * @return
	 * @see java.util.List#toArray(T[])
	 */
	public <T> T[] toArray(T[] array) {
		return data.toArray(array);
	}
	
	public boolean checkChangedNClear() {
		return this.changed.compareAndSet(true, false);
	}

	public boolean isChanged() {
		return this.changed.get();
	}
}
