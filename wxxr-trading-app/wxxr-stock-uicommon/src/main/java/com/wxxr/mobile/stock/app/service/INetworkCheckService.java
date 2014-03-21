package com.wxxr.mobile.stock.app.service;

public interface INetworkCheckService {
	boolean isNetworkConnected();
	boolean isWifiConnected();
	boolean is3GConnected();
	boolean isGSMConnected();
}
