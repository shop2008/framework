package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.bool;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;

@View(name = "userTradeRecordPage", withToolbar = true, description = "我的交易记录")
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

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 1}"),
			@Attribute(name = "textColor", value = "${curItemId == 1?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean sucRecordBtn;

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 2}"),
			@Attribute(name = "textColor", value = "${curItemId == 2?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean allRecordBtn;

	@Field(valueKey="visible", binding="${successTradeAccountListBean!=null?(successTradeAccountListBean.successTradingAccounts!=null?false:true):true}")
	boolean sucRecordNullVisible;
	
	@Menu(items = { "left", "right" })
	private IMenu toolbar;

	@Bean
	int curItemId = 1;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	/**
	 * 显示所有成功交易记录
	 * 
	 * @param event
	 * @return
	 */
	@Command
	String showSucRecords(InputEvent event) {
		curItemId = 1;
		registerBean("curItemId", curItemId);
		if (successTradeAccountListBean != null)
			getUIContext().getKernelContext()
					.getService(ITradingManagementService.class)
					.getMySuccessTradingAccountList(0, 2);
		return null;
	}

	/**
	 * 显示所有交易记录
	 * 
	 * @param event
	 * @return
	 */
	@Command
	String showAllRecords(InputEvent event) {
		curItemId = 2;
		registerBean("curItemId", curItemId);
		if (allTradeAccountListBean != null)
			getUIContext().getKernelContext()
					.getService(ITradingManagementService.class)
					.getMyAllTradingAccountList(0, 2);
		return null;
	}

	@Command(
			commandName = "allRecordItemClicked",
			navigations={
					@Navigation(on="operationDetails", showPage="OperationDetails"),
					@Navigation(on="SellOut",showPage="sellTradingAccount"),
					@Navigation(on="BuyIn",showPage="TBuyTradingPage")
				}
			)
	CommandResult allRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			GainBean allBean = null;

			// 本人
			if (allTradeAccountListBean != null) {
				List<GainBean> allTradeList = allTradeAccountListBean.getAllTradingAccounts();
				if (allTradeList != null && allTradeList.size() > 0) {
					allBean = allTradeList.get(position);
				}
			}
			CommandResult result = null;
			if (allBean != null) {
				/** 交易盘ID */
				Long accId = allBean.getTradingAccountId();
				String tradeStatus = allBean.getOver();
				Boolean isVirtual = allBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accid", accId);
				map.put("isVirtual", isVirtual);
				result.setPayload(map);
				if ("CLOSED".equals(tradeStatus)) {
					result.setResult("operationDetails");
				}
				if ("UNCLOSE".equals(tradeStatus)) {
					int status = allBean.getStatus();
					if (status == 0) {
						// 进入卖出界面
						result.setResult("SellOut");
					} else if (status == 1) {
						// 进入买入界面
						result.setResult("BuyIn");
					}
				}
			}
			return result;
		}
		return null;
	}

	@Command(
			commandName = "sucRecordItemClicked",
			navigations={
					@Navigation(on="operationDetails", showPage="OperationDetails"),
					@Navigation(on="SellOut",showPage="sellTradingAccount"),
					@Navigation(on="BuyIn",showPage="TBuyTradingPage")
				}
			)
	CommandResult sucRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			GainBean sucBean = null;

			// 本人
			if (successTradeAccountListBean != null) {
				List<GainBean> sucTradeList = successTradeAccountListBean.getSuccessTradingAccounts();
				if (sucTradeList != null && sucTradeList.size() > 0) {
					sucBean = sucTradeList.get(position);
				}
			}
			CommandResult result = null;
			if (sucBean != null) {
				/** 交易盘ID */
				Long accId = sucBean.getTradingAccountId();
				String tradeStatus = sucBean.getOver();
				Boolean isVirtual = sucBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accid", accId);
				map.put("isVirtual", isVirtual);
				result.setPayload(map);
				if ("CLOSED".equals(tradeStatus)) {
					result.setResult("operationDetails");
				}
				if ("UNCLOSE".equals(tradeStatus)) {
					int status = sucBean.getStatus();
					if (status == 0) {
						// 进入卖出界面
						result.setResult("SellOut");
					} else if (status == 1) {
						// 进入买入界面
						result.setResult("BuyIn");
					}
				}
			}
			return result;
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
