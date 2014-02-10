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
public interface IBizObjectStorage<K extends Number,T extends InheritableBizObject<K>, D extends INumberKeyObject<K>> {
	<E extends T> E get(K key);
	<E extends T> E load(K key);
	<E extends T> K saveOrUpdate(E bizObject);
	<E extends T> E newObject(String type,Map<String, Object> params);
	
	List<T> doQuery(String queryName, Map<String, Object> criteria);
	
	void registerExtStrategy(String type, IExtStrategy<K,T, ? extends T, D, ? extends INumberKeyObject<K>> strategy);
	
	boolean unregisterExtStrategy(String type, IExtStrategy<K,T, ? extends T,D, ? extends INumberKeyObject<K>> strategy);
}
