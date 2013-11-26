package com.wxxr.mobile.stock.app.common;

public interface IBindableEntityCache<K, V> {

	void registerCallback(ICacheUpdatedCallback cb);

	void unregisterCallback(ICacheUpdatedCallback cb);

	V getEntity(Object key);

	Object[] getAllKeys();

	Object[] getValues();

	void putEntity(K key, V value);

	V removeEntity(K key);

	int getCacheSize();

	void acceptVisitor(ICacheVisitor<V> visitor);

}