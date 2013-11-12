package com.wxxr.mobile.stock.client.binding;

public interface IRefreshCallback {
	void refreshSuccess();
	void refreshFailed(String message);
}
