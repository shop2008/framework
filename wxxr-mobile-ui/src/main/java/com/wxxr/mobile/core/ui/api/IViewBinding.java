package com.wxxr.mobile.core.ui.api;


public interface IViewBinding extends IBinding<IView> {
	IFieldBinding getFieldBinding(String fieldName);
	IMenuHandler getMenuHandler(String menuId);
	String getBindingViewId();
	boolean isOnShow();
}
