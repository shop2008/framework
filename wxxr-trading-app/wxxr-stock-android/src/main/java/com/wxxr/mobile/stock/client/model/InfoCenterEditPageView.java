package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.biz.StockSelection;


@View(name = "InfoCenterEditPageView", description = "行情中心")
@AndroidBinding(type = AndroidBindingType.ACTIVITY, layoutId = "R.layout.price_center_edit_page_layout")
public abstract class InfoCenterEditPageView extends PageBase implements IModelUpdater {

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "完成", icon = "") })
	String toolbarClickedLeft(InputEvent event) {
		return null;
	}

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "right", label = "搜索", icon = "resourceId:drawable/find_button_style") }, navigations = { @Navigation(on = "*", showPage = "GeGuStockPage") })
	String toolbarClickedSearch(InputEvent event) {
		updateSelection(new StockSelection());
		return "";
	}	
	
	
	@Override
	public void updateModel(Object value) {

	}

}
