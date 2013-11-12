package com.wxxr.mobile.core.ui.api;

import java.util.Map;

public interface IAppToolbar {
	void attachPage(IPage page);
	void dettachPage(IPage page);
	IPage getCurrentPage();
	void show();
	void hide();
	boolean isActive();
	void setTitle(String title,Map<String, String> parameters);

}
