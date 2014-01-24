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
import com.wxxr.mobile.core.ui.annotation.OnShow;
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

@View(name = "userTradeRecordPage", withToolbar = true, description = "交易记录", provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_trade_record_page_layout")
public abstract class UserTradeRecordPage extends PageBase {

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	/*@Field(valueKey = "visible", binding = "${allTradeAccountListBean!=null?(allTradeAccountListBean.data!=null?(allTradeAccountListBean.data.size()>0?true:false):false):false}")
	boolean recordNotNullVisible;*/


	@Bean(type = BindingType.Pojo, express = "${tradingService!=null?tradingService.getTotalGain(allStart,allLimit):null}")
	BindableListWrapper<GainBean> allTradeAccountListBean;

	@Bean(type = BindingType.Pojo, express = "${tradingService!=null?tradingService.getGain(sucStart,sucLimit):null}")
	BindableListWrapper<GainBean> successTradeAccountListBean;

	@Field(valueKey = "options",upateAsync=true,binding = "${allTradeAccountListBean!=null?allTradeAccountListBean.getData(true):null}", visibleWhen = "${curItemId==2}")
	List<GainBean> actualRecordsList;

	@Field(valueKey = "options",upateAsync=true, binding = "${successTradeAccountListBean!=null?successTradeAccountListBean.getData(true):null}", visibleWhen = "${curItemId==1}")
	List<GainBean> virtualRecordsList;

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 1}"),
			@Attribute(name = "textColor", value = "${curItemId == 1?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean sucRecordBtn;

	@Field(valueKey = "checked", attributes = {
			@Attribute(name = "checked", value = "${curItemId == 2}"),
			@Attribute(name = "textColor", value = "${curItemId == 2?'resourceId:color/white':'resourceId:color/gray'}") })
	boolean allRecordBtn;

	/*@Field(valueKey = "visible", binding = "${(curItemId == 1)&&(successTradeAccountListBean!=null?(successTradeAccountListBean.data!=null?(successTradeAccountListBean.data.size()>0?false:true):true):true)}")
	boolean sucRecordNullVisible;*/

	/*@Field(valueKey = "visible", binding = "${(curItemId == 2)&&(allTradeAccountListBean!=null?(allTradeAccountListBean.data!=null?(allTradeAccountListBean.data.size()>0?false:true):true):true)}")
	boolean wholeRecordNullVisible;*/
	
	@Menu(items = { "left", "right" })
	private IMenu toolbar;

	
	@Bean
	private int sucStart = 0;
	
	@Bean
	private int sucLimit = 20;
	
	@Bean
	private int allStart = 0;
	
	@Bean
	private int allLimit = 20;
	@Bean
	int curItemId = 1;
	
	@Field(valueKey = "text",visibleWhen = "${curItemId == 1}",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${successTradeAccountListBean!=null&&successTradeAccountListBean.data!=null&&successTradeAccountListBean.data.size()>0?true:false}")})
	String virtualRefreshView;
	
	@Field(valueKey = "text",visibleWhen = "${curItemId == 2}",attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${allTradeAccountListBean!=null&&allTradeAccountListBean.data!=null&&allTradeAccountListBean.data.size()>0?true:false}")})
	String actualRefreshView;

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
	//@ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 500, cancellable = true)
	String showSucRecords(InputEvent event) {
		showSucRecords(false);
		return null;
	}
	
	void showSucRecords(boolean wait4Finish) {
		curItemId = 1;
		registerBean("curItemId", curItemId);
		if (tradingService != null)
			tradingService.getGain(0, successTradeAccountListBean.getData().size(), wait4Finish);
	}
	@OnShow
	void initData() {
		
	}

	
	
	/**
	 * 显示所有交易记录
	 * 
	 * @param event
	 * @return
	 */
	@Command
	//@ExeGuard(title = "提示", message = "正在获取数据，请稍后...", silentPeriod = 500, cancellable = true)
	String showAllRecords(InputEvent event) {
		showAllRecords(false);
		return null;
	}

	void showAllRecords(boolean wait4Finish) {
		curItemId = 2;
		registerBean("curItemId", curItemId);
		if (tradingService != null)
			tradingService.getTotalGain(0, allTradeAccountListBean.getData().size(), wait4Finish);
	}
	
	@Command(commandName = "actualRecordItemClicked", navigations = { @Navigation(on = "operationDetails", showPage = "OperationDetails") })
	CommandResult actualRecordItemClicked(InputEvent event) {
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

	@Command(commandName = "virtualRecordsList", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails")
			})
	CommandResult virtualRecordsList(InputEvent event) {
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
	String handleVirtualRefresh(InputEvent event) {
		if(event.getEventType().equals("TopRefresh")) {
			showSucRecords(true);
		} else if(event.getEventType().equals("BottomRefresh")) {
			int completeSize = 0;
			if(successTradeAccountListBean != null)
				completeSize = successTradeAccountListBean.getData().size();
			sucStart = completeSize;
			if(tradingService != null) {
				tradingService.getGain(sucStart, sucLimit, true);
			}
		}
		return null;
	}

	@Command
	String handleActualRefresh(InputEvent event) {
		if(event.getEventType().equals("TopRefresh")) {
			showAllRecords(true);
		} else if(event.getEventType().equals("BottomRefresh")) {
			int completeSize = 0;
			if(allTradeAccountListBean != null)
				completeSize = allTradeAccountListBean.getData().size();
			allStart = completeSize;
			if(tradingService != null) {
				tradingService.getTotalGain(allStart, allLimit, true);
			}
		}
		return null;
	}

	
	@Command
	String handleRetryClick(InputEvent event) {
		if(curItemId  == 1) {
			if(tradingService != null) {
				int completeSize = successTradeAccountListBean.getData().size();
				tradingService.getGain(completeSize, sucLimit);
			}
		} else if(curItemId == 0) {
			int completeSize = allTradeAccountListBean.getData().size();
			tradingService.getTotalGain(completeSize, allLimit);
		}
		
		return null;
	}
}
