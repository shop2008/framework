package com.wxxr.mobile.core.ui.api;

import java.util.Map;

public interface IAppToolbar {
	void attach(IView page);
	void dettach(IView page);
	IView getCurrentAttachment();
	void show();
	void hide();
	boolean isActive();
	void setTitle(String title,Map<String, String> parameters);

}
