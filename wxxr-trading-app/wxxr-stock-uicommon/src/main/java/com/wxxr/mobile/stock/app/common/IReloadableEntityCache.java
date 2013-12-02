package com.wxxr.mobile.stock.app.common;

import java.util.Map;

public interface IReloadableEntityCache<K, V> extends IBindableEntityCache<K, V>{

	void doReloadIfNeccessay();
	
	void doReloadIfNeccessay(Map<String, Object> params);

	void forceReload(boolean wait4Finish);

	void forceReload(Map<String, Object> params, boolean wait4Finish);
	/**
	 * @return the lastUpdateTime
	 */
	long getLastUpdateTime();

	/**
	 * @return the minReloadIntervalInSeconds
	 */
	int getMinReloadIntervalInSeconds();

	/**
	 * @param minReloadIntervalInSeconds the minReloadIntervalInSeconds to set
	 */
	void setMinReloadIntervalInSeconds(int minReloadIntervalInSeconds);
	
	/**
	 * 
	 * @return -1 if this cache is not auto-reloading
	 */
	int getAutoReloadIntervalInSeconds();
	
	int getNumberOfActiveClient();

}