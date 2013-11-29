/*
 * @(#)LRUList.java	 2005-10-15
 *
 * Copyright 2004-2005 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.core.util;

import java.util.Iterator;
import java.util.LinkedList;


public class LRUList<E> implements Iterable<E>{
    
	private static class Element<E> {
		private E object;
		private long timestamp;
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@SuppressWarnings("rawtypes")
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Element){
				return object.equals(((Element)obj).object);
			}
			return false;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return object.hashCode();
		}
	}
	
    
    private LinkedList<Element<E>> queue;
    private long timeout;
    
    private int size;
    
    public LRUList(int size) {
        super();
        if(size <= 0){
            throw new IllegalArgumentException("Size should larger than 0");
        }
        this.size = size;   
        queue = new LinkedList<Element<E>>();   
    }
    
    public LRUList() {
        super();
        this.size = 0;   
        queue = new LinkedList<Element<E>>();   
    }


    public synchronized E get(){
        checkTimeout();
    	Element<E> obj = null;
        if(queue.size() > 0){
        	obj = queue.removeFirst();
            E ret = obj.object;
            return ret;
        }
        return null;
    }

    public synchronized void put(E obj){
        checkTimeout();
    	if(size > 0){
	        if(queue.size() == size){
	        	queue.removeFirst();
	        }
    	}
    	Element<E> elem = new Element<E>();
    	elem.object = obj;
    	elem.timestamp = System.currentTimeMillis();
        queue.addLast(elem);
    }
    
    public synchronized boolean contains(E obj){
    	checkTimeout();
		for(Iterator<Element<E>> itr = queue.iterator();itr.hasNext();){
			Element<E> elem = itr.next();
			if((elem != null)&&(elem.object == obj)){
				return true;
			}
		}
		return false;
    }

	/**
	 * 
	 * @see java.util.LinkedList#clear()
	 */
	public synchronized void clear() {
		queue.clear();
	}

	/**
	 * @return
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	public synchronized boolean isEmpty() {
		checkTimeout();
		return queue.isEmpty();
	}


	/**
	 * @return
	 * @see java.util.LinkedList#remove()
	 */
	public synchronized E remove() {
		checkTimeout();
		Element<E> elem = queue.remove();
		return elem != null ? elem.object : null;
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.LinkedList#remove(java.lang.Object)
	 */
	public synchronized boolean remove(E o) {
		checkTimeout();
		Element<E> elem = doRemove(o);
		if(elem != null){
			return true;
		}
		return false;
	}
	
	protected Element<E> doRemove(E o){
		for(Iterator<Element<E>> itr = queue.iterator();itr.hasNext();){
			Element<E> elem = itr.next();
			if((elem != null)&&(elem.object == o)){
				itr.remove();
				return elem;
			}
		}
		return null;
	}
	
	public synchronized boolean touch(E o){
		checkTimeout();
    	Element<E> elem = doRemove(o);
    	if(elem != null){
    		elem.timestamp = System.currentTimeMillis();
    		queue.addLast(elem);
    		return true;
    	}
    	return false;
		
	}
	/**
	 * 
	 * @return
	 */
    public synchronized Object peek() {
        checkTimeout();
    	Element<E> obj = null;
        if(queue.size() > 0){
        	obj = queue.getFirst();
            E ret = obj.object;
            return ret;
        }
        return null;

    }

    
    public synchronized Object[] toArray() {
    	checkTimeout();
        Object[] elems =  queue.toArray();
        if((elems == null)|| (elems.length == 0)){
        	return null;
        }
        Object[] a = new Object[elems.length];
        for(int i=0 ; i < elems.length ;i++){
        	@SuppressWarnings("unchecked")
			Element<E> elem = (Element<E>)elems[i];
        	if(elem != null){
        		a[i] = elem.object;
        	}
        }
        return a;
    }


	/**
	 * @param <E>
	 * @param a
	 * @return
	 * @see java.util.LinkedList#toArray(E[])
	 */
	@SuppressWarnings("unchecked")
	public synchronized E[] toArray(E[] a) {
		checkTimeout();
        Object[] elems =  queue.toArray();
        if((elems == null)|| (elems.length == 0)){
        	return null;
        }
        if (a.length < elems.length)
            a = (E[])java.lang.reflect.Array.newInstance(
                                a.getClass().getComponentType(), elems.length);
        for(int i=0 ; i < elems.length ;i++){
        	Element<E> elem = (Element<E>)elems[i];
        	if(elem != null){
        		a[i] = elem.object;
        	}
        }
        return a;
	}
	
	public synchronized int size(){
		checkTimeout();
		return queue.size();
	}
	
	public synchronized int maximumSize() {
		return size;
	}
	
	private void checkTimeout(){
		if(timeout == 0L){
			return;
		}
		Element<E> elem = null;
		while(!queue.isEmpty()){
			elem = queue.removeFirst();
			if((elem != null)&&(System.currentTimeMillis() - elem.timestamp)<timeout){
				queue.addFirst(elem);
				break;
			}
		}
	}

	/**
	 * @return the timeout
	 */
	public long getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public Iterator<E> iterator() {
		return new Iterator<E>() {
			Iterator<Element<E>> itr = queue.iterator();
			Element<E> currentElem;
			public boolean hasNext() {
				return itr.hasNext();
			}

			/* (non-Javadoc)
			 * @see java.util.Iterator#next()
			 */
			public E next() {
				currentElem = itr.next();
				return (currentElem != null) ? currentElem.object : null;
			}

			/* (non-Javadoc)
			 * @see java.util.Iterator#remove()
			 */
			public void remove() {
				itr.remove();
				if(currentElem != null){
					currentElem = null;
				}
			}
		
		};
	}
}