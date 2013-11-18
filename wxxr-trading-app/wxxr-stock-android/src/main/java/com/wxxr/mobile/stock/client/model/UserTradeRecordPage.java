package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;

@View(name = "userTradeRecordPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_trade_record_page_layout")
public abstract class UserTradeRecordPage extends PageBase {

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Field(valueKey = "visible", binding = "${tradingAccount!=null?tradingAccount.allTradingAccounts!=null?true:false:false}")
	boolean recordNotNullVisible;

	@Field(valueKey = "visible", binding = "${tradingAccount!=null?tradingAccount.allTradingAccounts!=null?false:true:true}")
	boolean recordNullVisible;

	@Bean(type = BindingType.Pojo, express = "${tradingService!=null?tradingService.myTradingAccountList:null}", nullable = true)
	TradingAccountListBean tradingAccount;

	@Field(valueKey = "options", binding = "${tradingAccount!=null?tradingAccount.allTradingAccounts:null}", visibleWhen = "${curItemId==2}")
	List<TradingAccountBean> allRecordsList;

	@Field(valueKey = "options", binding = "${tradingAccount!=null?tradingAccount.successTradingAccountBeans:null}", visibleWhen = "${curItemId==1}")
	List<TradingAccountBean> successRecordsList;

	@Field(valueKey = "checked", attributes = {@Attribute(name = "checked", value = "${curItemId==1}") })
	boolean sucRecordBtn;

	@Field(valueKey = "checked", attributes = {@Attribute(name = "checked", value = "${curItemId==2}") })
	boolean allRecordBtn;

	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "back", description = "Back To Last UI")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator()
					.hidePage(this);
		}
		return null;
	}

	int curItemId = 1;

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
		if (tradingService != null)
			tradingService.getMyTradingAccountList().getSuccessTradingAccountBeans();
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
		if (tradingService != null)
			tradingService.getMyTradingAccountList().getAllTradingAccounts();
		return null;
	}

	@Command(commandName = "allRecordItemClicked")
	String allRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			if (allRecordsList != null && allRecordsList.size() > 0) {
				TradingAccountBean bean = allRecordsList.get(position);
				System.out.println("-----" + bean.getType());
			}

		}
		return null;
	}

	@Command(commandName = "sucRecordItemClicked")
	String sucRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			if (successRecordsList != null && successRecordsList.size() > 0) {
				TradingAccountBean bean = successRecordsList.get(position);
				System.out.println("-----" + bean.getType());
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
