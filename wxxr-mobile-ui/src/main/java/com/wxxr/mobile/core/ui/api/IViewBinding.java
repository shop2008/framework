package com.wxxr.mobile.core.ui.api;


public interface IViewBinding extends IBinding<IView> {
	<T extends IUIComponent> IBinding<T> getFieldBinding(String fieldName);
	IMenuHandler getMenuHandler(String menuId);
}
