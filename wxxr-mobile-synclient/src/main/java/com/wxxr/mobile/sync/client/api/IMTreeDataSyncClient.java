/**
 * 
 */
package com.wxxr.mobile.sync.client.api;

/**
 * @author neillin
 *
 */
public interface IMTreeDataSyncClient {
		
	void clearCache(String key);
	
	void registerConsumer(String key, IDataConsumer provider);
	
	boolean unregisterConsumer(String key, IDataConsumer provider);
}
