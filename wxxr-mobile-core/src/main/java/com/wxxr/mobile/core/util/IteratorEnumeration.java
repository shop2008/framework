/**
 * 
 */
package com.wxxr.mobile.core.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author neillin
 *
 */
public class IteratorEnumeration<E> implements Enumeration<E> {
	
	private final Iterator<E> itr;
	
	public IteratorEnumeration(Iterator<E> i){
		this.itr = i;
	}

	@Override
	public boolean hasMoreElements() {
		return (itr != null)&&itr.hasNext();
	}

	@Override
	public E nextElement() {
		return itr.next();
	}

}
