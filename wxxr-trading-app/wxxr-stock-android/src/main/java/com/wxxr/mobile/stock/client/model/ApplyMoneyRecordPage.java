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
import com.wxxr.mobile.stock.app.bean.DrawMoneyRecordBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

@View(name = "ApplyMoneyRecordPage" ,withToolbar=true, description="提现记录")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.apply_money_record_layout")
public abstract class ApplyMoneyRecordPage extends PageBase {

	
	@Field(valueKey="text",attributes = {
			@Attribute(name = "enablePullDownRefresh", value = "true"),
			@Attribute(name = "enablePullUpRefresh", value = "${(applyMoneyRecordsBean!=null&&applyMoneyRecordsBean.data!=null&&applyMoneyRecordsBean.data.size()>0)?true:false}") })
	String refreshView;
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo ,express="${tradingService!=null?tradingService.getDrawMoneyRecordList(0,20,false):null}")
	BindableListWrapper<DrawMoneyRecordBean> applyMoneyRecordsBean;
	
	@Field(valueKey="options",binding="${applyMoneyRecordsBean!=null?applyMoneyRecordsBean.data:null}")
	List<DrawMoneyRecordBean> applyMoneyRecords;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Command
	String handleRefresh(InputEvent event) {
		
		if(event.getEventType().equals("TopRefresh")) {
			//下拉刷新 
			if(tradingService != null) {
				tradingService.getDrawMoneyRecordList(0, applyMoneyRecordsBean.getData().size(), true);
			}
		} else if(event.getEventType().equals("BottomRefresh")) {
			//上拉加载更多
			if(tradingService != null) {
				tradingService.getDrawMoneyRecordList(applyMoneyRecordsBean.getData().size(), 20, true);
			}
		}
		return null;
	}
}
