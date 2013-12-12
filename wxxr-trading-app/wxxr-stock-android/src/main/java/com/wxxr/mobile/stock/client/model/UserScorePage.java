package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.VoucherDetailsBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="userScorePage", withToolbar=true, description="我的积分")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_score_layout")
public abstract class UserScorePage extends PageBase {

	@Field(valueKey="text", binding="${voucherDetailsBean!=null?voucherDetailsBean.addToday:'--'}", converter="stockL2StrConvertor")
	String todayIncrease;
	
	@Field(valueKey="text", binding="${voucherDetailsBean!=null?voucherDetailsBean.reduceToday:'--'}", converter="stockL2StrConvertor")
	String todayDecrease;
	
	@Field(valueKey="text",binding="${voucherDetailsBean!=null?voucherDetailsBean.bal:'--'}", converter="stockL2StrConvertor")
	String todayBalance; 
	
	@Convertor(params={
			
			@Parameter(name="format", value="%.0f"),
			@Parameter(name="nullString",value="0")
	})
	StockLong2StringConvertor stockL2StrConvertor;
	@Bean(type=BindingType.Pojo, express="${tradingService.getVoucherDetails(0,10)}")
	VoucherDetailsBean voucherDetailsBean;
	
	
	@Bean(type=BindingType.Service)
	ITradingManagementService  tradingService;
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	@Command(navigations={@Navigation(on="*", showPage="uRealPanelScorePage")})
	String toScoreDetail(InputEvent event) {
		return "*";
	}
}
