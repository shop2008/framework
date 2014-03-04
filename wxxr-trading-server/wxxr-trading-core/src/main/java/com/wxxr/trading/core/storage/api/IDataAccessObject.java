/**
 * 
 */
package com.wxxr.trading.core.storage.api;

import java.util.List;
import java.util.Map;

import com.wxxr.smbiz.bizobject.INumberKeyObject;

/**
 * @author neillin
 *
 */
public interface IDataAccessObject<K extends Number,T extends INumberKeyObject<K>> {
	T findByPrimaryKey(K key);
	void update(T object);
	void remove(T object);
	K add(T object);
	String getType(T object);
	String[] getQueryNames();
	boolean hasQuery(String queryName);
	List<T> doQuery(String name, Map<String, Object> criteria);
}
