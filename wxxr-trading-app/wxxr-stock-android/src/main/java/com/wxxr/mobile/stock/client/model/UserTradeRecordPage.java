package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;

@View(name = "userTradeRecordPage", withToolbar=true, description="我的交易记录")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_trade_record_page_layout")
public abstract class UserTradeRecordPage extends PageBase {

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Field(valueKey = "visible", binding = "${allTradeAccountListBean!=null?allTradeAccountListBean.allTradingAccounts!=null?true:false:false}")
	boolean recordNotNullVisible;

	@Field(valueKey = "visible", binding = "${allTradeAccountListBean!=null?allTradeAccountListBean.allTradingAccounts!=null?false:true:true}")
	boolean recordNullVisible;

	@Bean(type = BindingType.Pojo, express = "${tradingService!=null?tradingService.getMyAllTradingAccountList(0,2):null}", nullable = true)
	TradingAccountListBean allTradeAccountListBean;
	
	@Bean(type = BindingType.Pojo, express = "${tradingService!=null?tradingService.getMySuccessTradingAccountList(0,2):null}", nullable = true)
	TradingAccountListBean successTradeAccountListBean;

	@Field(valueKey = "options", binding = "${allTradeAccountListBean!=null?allTradeAccountListBean.allTradingAccounts:null}", visibleWhen = "${curItemId==2}")
	List<GainBean> allRecordsList;

	@Field(valueKey = "options", binding = "${successTradeAccountListBean!=null?successTradeAccountListBean.successTradingAccounts:null}", visibleWhen = "${curItemId==1}")
	List<GainBean> successRecordsList;

	@Field(valueKey = "checked",  attributes={
			@Attribute(name = "checked", value = "${curItemId == 1}"),
			@Attribute(name = "textColor", value = "${curItemId == 1?'resourceId:color/white':'resourceId:color/gray'}")
			})
	boolean sucRecordBtn;

	@Field(valueKey = "checked", attributes={
			@Attribute(name = "checked", value = "${curItemId == 2}"),
			@Attribute(name = "textColor", value = "${curItemId == 2?'resourceId:color/white':'resourceId:color/gray'}")
			})
	boolean allRecordBtn;


	int curItemId = 1;
		
	
	@Menu(items={"left","right"})
	private IMenu toolbar;
	
	
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);	
		return null;
	}

	@OnShow
	protected void initCurItemId() {
		registerBean("curItemId", curItemId);
	}

	/**
	 * 显示所有成功交易记录
	 * 
	 * @param event
	 * @return
	 */
	String showSucRecords(InputEvent event) {
		curItemId = 1;
		registerBean("curItemId", curItemId);
		if (successTradeAccountListBean != null)
			getUIContext().getKernelContext().getService(ITradingManagementService.class).getMySuccessTradingAccountList(0, 2);
		return null;
	}

	/**
	 * 显示所有交易记录
	 * 
	 * @param event
	 * @return
	 */
	String showAllRecords(InputEvent event) {

		curItemId = 2;
		registerBean("curItemId", curItemId);
		if (allTradeAccountListBean != null)
			getUIContext().getKernelContext().getService(ITradingManagementService.class).getMyAllTradingAccountList(0, 2);
		return null;
	}

	@Command(commandName = "allRecordItemClicked")
	String allRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			if (allRecordsList != null && allRecordsList.size() > 0) {
				GainBean bean = allRecordsList.get(position);
				System.out.println("-----------"+bean.getMaxStockName());
			}

		}
		return null;
	}

	@Command(commandName = "sucRecordItemClicked")
	String sucRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			if (successRecordsList != null && successRecordsList.size() > 0) {
				GainBean bean = successRecordsList.get(position);
				System.out.println("-----" + bean.getMaxStockName());
			}
		}
		return null;
	}

	@Command
	String handleSucTopRefresh(InputEvent event) {
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		showSucRecords(null);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}

	@Command
	String handleAllTopRefresh(InputEvent event) {
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		showAllRecords(null);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}
}
