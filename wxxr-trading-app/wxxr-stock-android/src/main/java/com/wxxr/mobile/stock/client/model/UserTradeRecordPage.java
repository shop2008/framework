package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;

@View(name = "userTradeRecordPage", withToolbar = true, description = "我的交易记录", provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_trade_record_page_layout")
public abstract class UserTradeRecordPage extends PageBase {

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Field(valueKey = "visible", binding = "${allTradeAccountListBean!=null?(allTradeAccountListBean.data!=null?(allTradeAccountListBean.data.size()>0?true:false):false):false}")
	boolean recordNotNullVisible;

	@Field(valueKey = "visible", binding = "${allTradeAccountListBean!=null?(allTradeAccountListBean.data!=null?(allTradeAccountListBean.data.size()>0?false:true):true):true}")
	boolean recordNullVisible;

	@Bean(type = BindingType.Pojo, express = "${tradingService!=null?tradingService.getTotalGain(allStart,allLimit):null}")
	BindableListWrapper<GainBean> allTradeAccountListBean;

	@Bean(type = BindingType.Pojo, express = "${tradingService!=null?tradingService.getGain(sucStart,sucLimit):null}")
	BindableListWrapper<GainBean> successTradeAccountListBean;

	@Field(valueKey = "options", binding = "${allTradeAccountListBean!=null?allTradeAccountListBean.data:null}", visibleWhen = "${curItemId==2}")
	List<GainBean> allRecordsList;

	@Field(valueKey = "options", binding = "${successTradeAccountListBean!=null?successTradeAccountListBean.data:null}", visibleWhen = "${curItemId==1}")
	List<GainBean> successRecordsList;

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 1}"),
			@Attribute(name = "textColor", value = "${curItemId == 1?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean sucRecordBtn;

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 2}"),
			@Attribute(name = "textColor", value = "${curItemId == 2?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean allRecordBtn;

	@Field(valueKey = "visible", binding = "${successTradeAccountListBean!=null?(successTradeAccountListBean.data!=null?(successTradeAccountListBean.data.size()>0?false:true):true):true}")
	boolean sucRecordNullVisible;

	@Menu(items = { "left", "right" })
	private IMenu toolbar;

	
	@Bean
	private int sucStart = 0;
	
	@Bean
	private int sucLimit = 10;
	
	@Bean
	private int allStart = 0;
	
	@Bean
	private int allLimit = 10;
	@Bean
	int curItemId = 1;
	
	@Field(valueKey = "text",visibleWhen = "${curItemId == 1}",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${successTradeAccountListBean!=null&&successTradeAccountListBean.data!=null&&successTradeAccountListBean.data.size()>0?true:false}")})
	String sucRefreshView;
	
	@Field(valueKey = "text",visibleWhen = "${curItemId == 2}",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${allTradeAccountListBean!=null&&allTradeAccountListBean.data!=null&&allTradeAccountListBean.data.size()>0?true:false}")})
	String allRefreshView;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
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
		if (tradingService != null)
			tradingService.getGain(0, successTradeAccountListBean.getData().size());
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
		if (tradingService != null)
			tradingService.getTotalGain(0, allTradeAccountListBean.getData().size());
		return null;
	}

	@Command(commandName = "allRecordItemClicked", navigations = { @Navigation(on = "operationDetails", showPage = "OperationDetails") })
	CommandResult allRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			GainBean allBean = null;

			// 本人
			if (allTradeAccountListBean != null) {
				List<GainBean> allTradeList = allTradeAccountListBean.getData();
				if (allTradeList != null && allTradeList.size() > 0) {
					allBean = allTradeList.get(position);
				}
			}
			CommandResult result = null;
			if (allBean != null) {
				/** 交易盘ID */
				Long accId = allBean.getTradingAccountId();
				Boolean isVirtual = allBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				if (accId != null)
					map.put("accid", accId);
				if (isVirtual != null)
					map.put("isVirtual", isVirtual);
				result.setPayload(map);
				result.setResult("operationDetails");
				updateSelection(new AccidSelection(String.valueOf(accId), isVirtual));
			}
			return result;
		}
		return null;
	}

	@Command(commandName = "sucRecordItemClicked", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails")
			})
	CommandResult sucRecordItemClicked(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			GainBean sucBean = null;

			// 本人
			if (successTradeAccountListBean != null) {
				List<GainBean> sucTradeList = successTradeAccountListBean
						.getData();
				if (sucTradeList != null && sucTradeList.size() > 0) {
					sucBean = sucTradeList.get(position);
				}
			}
			CommandResult result = null;
			if (sucBean != null) {
				/** 交易盘ID */
				Long accId = sucBean.getTradingAccountId();
				Boolean isVirtual = sucBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				if(accId != null)
					map.put("accid", accId);
				if(isVirtual != null)
					map.put("isVirtual", isVirtual);
				result.setPayload(map);
				result.setResult("operationDetails");
				updateSelection(new AccidSelection(String.valueOf(accId), isVirtual));
				//updateSelection(new StockSelection(String.valueOf(accId), isVirtual));
			}
			return result;
		}
		return null;
	}

	@Command
	String handleSucTopRefresh(InputEvent event) {
		if(event.getEventType().equals("TopRefresh")) {
			showSucRecords(event);
		} else if(event.getEventType().equals("BottomRefresh")) {
			int completeSize = 0;
			if(successTradeAccountListBean != null)
				completeSize = successTradeAccountListBean.getData().size();
			sucStart += completeSize;
			if(tradingService != null) {
				tradingService.getGain(sucStart, sucLimit);
			}
		}
		return null;
	}

	@Command
	String handleAllTopRefresh(InputEvent event) {
		if(event.getEventType().equals("TopRefresh")) {
			showAllRecords(event);
		} else if(event.getEventType().equals("BottomRefresh")) {
			int completeSize = 0;
			if(allTradeAccountListBean != null)
				completeSize = allTradeAccountListBean.getData().size();
			allStart += completeSize;
			if(tradingService != null) {
				tradingService.getGain(allStart, allLimit);
			}
		}
		return null;
	}

	@Command
	String handleSucBottomRefresh(InputEvent event) {
		
		int completeSize = 0;
		if(successTradeAccountListBean != null)
			completeSize = successTradeAccountListBean.getData().size();
		sucStart += completeSize;
		if(tradingService != null) {
			tradingService.getGain(sucStart, sucLimit);
		}
		return null;
	}
	
	@Command
	String handleAllBottomRefresh(InputEvent event) {
		int completeSize = 0;
		if(allTradeAccountListBean != null)
			completeSize = allTradeAccountListBean.getData().size();
		allStart += completeSize;
		if(tradingService != null) {
			tradingService.getGain(allStart, allLimit);
		}
		return null;
	}
}
