package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="TBuyTdTradingPageView",withToolbar=true, description="挑战交易盘T+D")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.buy_td_trading_page_view")
public abstract class TBuyTdTradingPageView extends PageBase implements IModelUpdater {

	@Menu(items={"left","right"})
	private IMenu toolbar;
	
 
	@Command(description="Invoke when a toolbar item was clicked",uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button_style")
			}
	)
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}	

	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "right", label = "交易详情", icon = "resourceId:drawable/message_button_style") }, 
			navigations = { @Navigation(on = "*", showPage = "TradingRecordsPage")
			})
	CommandResult toolbarClickedRight(InputEvent event) {
		return null;
	}	
	
	
	@Override
	public void updateModel(Object value) {

	}
}
