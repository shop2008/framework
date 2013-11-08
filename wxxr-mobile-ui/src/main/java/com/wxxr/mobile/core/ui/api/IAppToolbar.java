package com.wxxr.mobile.core.ui.api;

public interface IAppToolbar {
	void setCurrentPage(IPage page);
	IPage getCurrentPage();
	void show();
	void hide();
	boolean isActive();
}
