package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

@View(name="userScorePage", withToolbar=true, description="我的积分")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_score_layout")
public abstract class UserScorePage extends PageBase {

	
	@Bean(type=BindingType.Service)
	ITradingManagementService  tradingService;
	
	@Bean(type=BindingType.Pojo, express="${tradingService.getGainPayDetailDetails(0,20)}")
	BindableListWrapper<GainPayDetailBean> voucherDetailsBean;
	
	@Field(valueKey="options", binding="${voucherDetailsBean!=null?voucherDetailsBean.getData(true):null}", upateAsync=true)
	List<GainPayDetailBean> realScoreDetails;
	
	@Field(valueKey = "text",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${voucherDetailsBean!=null&&voucherDetailsBean.data!=null&&voucherDetailsBean.data.size()>0?true:false}")})
	String refreshView;
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	@Command
	String handleRefresh(InputEvent event) {
		if(event.getEventType().equals("TopRefresh")) {
			if(tradingService!=null && voucherDetailsBean!=null && voucherDetailsBean.getData()!=null) 
				tradingService.getGainPayDetailDetails(0, voucherDetailsBean.getData().size(), true);
		} else if(event.getEventType().equals("BottomRefresh")) {
			
			if(tradingService!=null && voucherDetailsBean!=null && voucherDetailsBean.getData()!=null) {
				tradingService.getGainPayDetailDetails(voucherDetailsBean.getData().size(), 20, true);
			}
		}
		return null;
	}
}
