/**
 * 
 */
package com.wxxr.mobile.core.bean.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author neillin
 *
 */
public class MapDecorator<K, V> implements Map<K, V> {

	private Map<K,V> data;
	final private PropertyChangeSupport support;
	final private String name;
	
	public MapDecorator(String propertyName,PropertyChangeSupport p){
		this.support = p;
		this.name = propertyName;
	}

	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		data.clear();
		this.support.firePropertyChange(name,  this, null);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return data.containsValue(value);
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return data.entrySet();
	}

	/**
	 * @param object
	 * @return
	 * @see java.util.Map#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		return data.equals(object);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public V get(Object key) {
		return data.get(key);
	}

	/**
	 * @return
	 * @see java.util.Map#hashCode()
	 */
	public int hashCode() {
		return data.hashCode();
	}

	/**
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	public Set<K> keySet() {
		return data.keySet();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public V put(K key, V value) {
		V val = data.put(key, value);
		this.support.fireIndexedPropertyChange(name,1, val, value);
		return val;
	}

	/**
	 * @param arg0
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends K, ? extends V> arg0) {
		data.putAll(arg0);
		this.support.firePropertyChange(name, null, arg0);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public V remove(Object key) {
		V val = data.remove(key);
		this.support.firePropertyChange(name, val, null);
		return val;
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	public int size() {
		return data.size();
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	public Collection<V> values() {
		return data.values();
	}


}
