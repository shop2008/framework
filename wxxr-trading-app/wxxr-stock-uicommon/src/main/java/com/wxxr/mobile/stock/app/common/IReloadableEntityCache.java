package com.wxxr.mobile.stock.app.common;

import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;

public interface IReloadableEntityCache<K, V> extends IBindableEntityCache<K, V>{

	/**
	 * 
	 * @param forceReload : true -> 无条件重新刷新， false按照既定的刷新条件刷新
	 * @param params : 刷新参数
	 * @param callback : 回调，true -> 刷新成功，有新数据， false -> 刷新成功，无新数据。可以为空
	 */
	void doReload(boolean forceReload, Map<String, Object> params, IAsyncCallback<Boolean> callback);
	
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
	
	boolean isStopAutoReloadIfNotActiveClient();
	
	void setStopAutoReloadIfNotActiveClient(boolean bool);

}