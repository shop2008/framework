package com.wxxr.mobile.stock.app.common;

public interface IReloadableEntityCache<K, V> extends IBindableEntityCache<K, V>{

	void doReloadIfNeccessay();

	void forceReload(boolean wait4Finish);

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

}