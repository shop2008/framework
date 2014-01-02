package com.wxxr.mobile.stock.client.service;

public interface IClientInfoService {

	/**
	 * 更新提示更新状态
	 * @param isEnabled 
	 */
	void setAlertUpdateEnabled(boolean isEnabled);
	
	/**
	 * 是否提示更新
	 */
	boolean alertUpdateEnabled();
}
