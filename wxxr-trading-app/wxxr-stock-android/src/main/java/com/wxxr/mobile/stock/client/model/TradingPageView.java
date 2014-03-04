package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.biz.StockSelection;

@View(name = "TradingPageView", withToolbar = true, description="交易", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT, layoutId = "R.layout.trading_page_layout")
public abstract class TradingPageView extends ViewBase implements IModelUpdater {

	
	@Menu(items={"right"})
	private IMenu toolbar;
	
 
	@Command(description="Invoke when a toolbar item was clicked",uiItems={
				@UIItem(id="right",label="搜索",icon="resourceId:drawable/find_button_style",visibleWhen="${true}")
			},navigations = { @Navigation(on = "*", showPage = "GeGuStockPage")}
	)
	String toolbarClickedLeft(InputEvent event) {
		updateSelection(new StockSelection());
		return "";
	}
	
	@OnShow
	void initToolbar() {
//		IPage page = AppUtils.getService(IWorkbenchManager.class).getPageNavigator().getCurrentActivePage();
//		page.getPageToolbar().show();
	}
	@Override
	public void updateModel(Object value) {
		
	}
	
	/**参赛模拟盘*/
	@Command(navigations={
			@Navigation(on = "CreateCanSaiTadingPageView", showPage="CreateCanSaiTadingPageView"),
			@Navigation(on = "*", message = "请先登录", params = {
					@Parameter(name = "title", value = "提示"),
					@Parameter(name = "onOK", value = "leftok"),
					@Parameter(name = "onCanceled", value = "取消")})
	})
	@SecurityConstraint(allowRoles={})
	@NetworkConstraint
	String handleSimulateTradingClick(InputEvent event){
		return "CreateCanSaiTadingPageView";
	}
	
	
	/**挑战交易盘T+1*/
	@Command(navigations={
			@Navigation(on = "CreateT1TradingPageView", showPage="CreateT1TradingPageView"),
			@Navigation(on = "*", message = "请先登录", params = {
					@Parameter(name = "title", value = "提示"),
					@Parameter(name = "onOK", value = "leftok"),
					@Parameter(name = "onCanceled", value = "取消")})
	})
	@SecurityConstraint(allowRoles={})
	@NetworkConstraint
	String handleT1Click(InputEvent event){
		return "CreateT1TradingPageView";
	}
	
	
	/**挑战交易盘T+3*/
	@Command(navigations={
			@Navigation(on = "CreateT3TradingPageView", showPage="CreateT3TradingPageView"),
			@Navigation(on = "*", message = "请先登录", params = {
					@Parameter(name = "title", value = "提示"),
					@Parameter(name = "onOK", value = "leftok"),
					@Parameter(name = "onCanceled", value = "取消")})
	})
	@SecurityConstraint(allowRoles={})
	@NetworkConstraint
	String handleT3Click(InputEvent event){
		return "CreateT3TradingPageView";
	}
	
	/**挑战交易盘T+D*/
	@Command(navigations={
			@Navigation(on = "CreateTDTradingPageView", showPage="CreateTDTradingPageView"),
			@Navigation(on = "*", message = "请先登录", params = {
					@Parameter(name = "title", value = "提示"),
					@Parameter(name = "onOK", value = "leftok"),
					@Parameter(name = "onCanceled", value = "取消")})
	})
	@SecurityConstraint(allowRoles={})
	@NetworkConstraint
	String handleTDClick(InputEvent event){
		return "CreateTDTradingPageView";
	}
	
	@Command(uiItems=@UIItem(id="leftok",label="确定",icon=""),navigations={
		@Navigation(on="*",showPage="userLoginPage")
	})
	String onCancelClick(InputEvent event){
		IView v = (IView)event.getProperty(InputEvent.PROPERTY_SOURCE_VIEW);
		if(v != null)
			v.hide();
		return "";
	}	
}
