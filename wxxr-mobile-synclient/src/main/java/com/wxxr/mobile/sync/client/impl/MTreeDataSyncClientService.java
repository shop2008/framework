/**
 * 
 */
package com.wxxr.mobile.sync.client.impl;

import com.wxxr.mobile.core.api.IDataExchangeCoordinator;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.sync.client.api.AbstractMTreeDataSyncClient;
import com.wxxr.mobile.sync.client.api.IMTreeDataSyncServerConnector;

/**
 * @author wangxuyang
 *
 */
public abstract class MTreeDataSyncClientService extends AbstractMTreeDataSyncClient {
	private IMTreeDataSyncServerConnector connector;
	private IKernelContext context;
	private long wifi_checkInterval = 10*60,failureCheckInterval = 20;//unit:seconds
	private long non_wifi_checkInterval = 60*60;
	@Override
	protected boolean isOK2CheckerMTreeServer() {
		long checkInterval = wifi_checkInterval;
		if (context.getService(IDataExchangeCoordinator.class).checkAvailableNetwork()!=IDataExchangeCoordinator.NETWORK_ID_WIFI) {
			checkInterval = non_wifi_checkInterval;
		}
		long lastCheckTime = 0L;
		long interval = checkInterval*1000L;
		if(isInDebugMode()){
			lastCheckTime = (getLastFailedTime() >= getLastSuccessTime()) ? getLastFailedTime() : getLastSuccessTime();
			interval = 10*1000L;
		} else if((getLastFailedTime() >= getLastSuccessTime())){
			lastCheckTime = getLastFailedTime();
			interval = failureCheckInterval*1000L;
		}else{
			lastCheckTime = getLastSuccessTime();
		}
		if((System.currentTimeMillis() - lastCheckTime) < interval){
			return false;
		}
		return context.getService(IDataExchangeCoordinator.class).checkAvailableNetwork() > 0;
	}

	@Override
	protected IMTreeDataSyncServerConnector getConnector() {
		if (connector == null) {
			connector = context.getService(IMTreeDataSyncServerConnector.class);
		}
		return connector;
	}
	public void init(IKernelContext context){
		if (context == null) {
			throw new IllegalArgumentException("context is null!!!");
		}
		this.context = context;
		log.info(String.format("WIFI Network check interval(unit:second) is %d ", wifi_checkInterval));
		log.info(String.format("NON WIFI Network check interval(unit:second) is %d ", non_wifi_checkInterval));
	}
	
	public void destory(){
		this.context = null;
		this.connector = null;
	}

	/**
	 * @return the failureCheckInterval
	 */
	public long getFailureCheckInterval() {
		return failureCheckInterval;
	}

	/**
	 * @param failureCheckInterval the failureCheckInterval to set
	 */
	public void setFailureCheckInterval(long failureCheckInterval) {
		this.failureCheckInterval = failureCheckInterval;
	}

	public long getWifi_checkInterval() {
		return wifi_checkInterval;
	}

	public void setWifi_checkInterval(long wifi_checkInterval) {
		this.wifi_checkInterval = wifi_checkInterval;
	}

	public long getNon_wifi_checkInterval() {
		return non_wifi_checkInterval;
	}

	public void setNon_wifi_checkInterval(long non_wifi_checkInterval) {
		this.non_wifi_checkInterval = non_wifi_checkInterval;
	}
	
	protected abstract boolean isInDebugMode();
}
