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

@View(name="uRealPanelScorePage", withToolbar=true, description="实盘积分明细")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.actual_panel_integral_detail_layout")
public abstract class URealPanelScorePage extends PageBase{

	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Field(valueKey="options", binding="${voucherDetailsBean!=null?voucherDetailsBean.getData(true):null}", upateAsync=true)
	List<GainPayDetailBean> actualScores;
	
	@Bean(type=BindingType.Pojo, express="${tradingService.getGainPayDetailDetails(start,limit)}")
	BindableListWrapper<GainPayDetailBean> voucherDetailsBean;
	
	@Field(attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${voucherDetailsBean!=null&&voucherDetailsBean.data!=null&&voucherDetailsBean.data.size()>0?true:false}")})
	String acctRefreshView;
	
	
	
	@Bean
	int start = 0;
	
	@Bean
	int limit = 20;
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	@Command
	String refreshTopData(InputEvent event) {
		
		if(event.getEventType().equals("TopRefresh")) {
			if(tradingService != null) {
				tradingService.getGainPayDetailDetails(0, voucherDetailsBean.getData().size());
			}
		} else if(event.getEventType().equals("BottomRefresh")) {
			int completeSize = 0;
			if(voucherDetailsBean != null)
				completeSize = voucherDetailsBean.getData().size();
			start = completeSize;
			if(tradingService != null) {
				tradingService.getGainPayDetailDetails(start, limit);
			}
		}
		return null;
	}
	
	@Command
	String refreshBottomData(InputEvent event) {
		int completeSize = 0;
		if(voucherDetailsBean != null)
			completeSize = voucherDetailsBean.getData().size();
		start = completeSize;
		if(tradingService != null) {
			tradingService.getGainPayDetailDetails(start, limit);
		}
		return null;
	}
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		
		if(tradingService != null) {
			tradingService.getGainPayDetailDetails(0, limit);
		}
		return null;
	}
}
