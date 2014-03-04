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
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ELBeanValueEvaluator;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;

@View(name = "UserTradeRecordPage", withToolbar = true, description = "交易记录", provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_trade_record_page_layout")
public abstract class UserTradeRecordPage extends PageBase {

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	/*@Field(valueKey = "visible", binding = "${allTradeAccountListBean!=null?(allTradeAccountListBean.data!=null?(allTradeAccountListBean.data.size()>0?true:false):false):false}")
	boolean recordNotNullVisible;*/


	@Bean(type = BindingType.Pojo, express = "${tradingService!=null?tradingService.getTotalGain(0,20,true):null}", effectingFields="virtualRecordsList")
	BindableListWrapper<GainBean> allTradeAccountListBean;

	@Bean(type = BindingType.Pojo, express = "${tradingService!=null?tradingService.getGain(0,20,true):null}", effectingFields="actualRecordsList")
	BindableListWrapper<GainBean> successTradeAccountListBean;

	@Field(valueKey = "options",binding = "${allTradeAccountListBean!=null?allTradeAccountListBean.getData():null}", visibleWhen = "${curItemId==2}")
	List<GainBean> actualRecordsList;

	@Field(valueKey = "options", binding = "${successTradeAccountListBean!=null?successTradeAccountListBean.getData():null}", visibleWhen = "${curItemId==1}")
	List<GainBean> virtualRecordsList;
	
	private ELBeanValueEvaluator<BindableListWrapper> allTradeAccountListBeanUpdater;
	private ELBeanValueEvaluator<BindableListWrapper> successTradeAccountListBeanUpdater;
	
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
	
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Bean
	boolean sucessNoMoreDataAlert = false;
	
	@Bean
	boolean allNoMoreDataAlert = false;
	

	private int oldDataSize = 0;

	private int newDataSize = 0;
	
	@Bean
	int curItemId = 1;
	
	@Field(valueKey = "text",visibleWhen = "${curItemId == 1}",attributes= {@Attribute(name="noMoreDataAlert", value="${sucessNoMoreDataAlert}"),@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${successTradeAccountListBean!=null&&successTradeAccountListBean.data!=null&&successTradeAccountListBean.data.size()>0?true:false}")})
	String virtualRefreshView;
	
	@Field(valueKey = "text",visibleWhen = "${curItemId == 2}",attributes= {@Attribute(name="noMoreDataAlert", value="${allNoMoreDataAlert}"),@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "${allTradeAccountListBean!=null&&allTradeAccountListBean.data!=null&&allTradeAccountListBean.data.size()>0?true:false}")})
	String actualRefreshView;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
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
		closeAlert();
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
		closeAlert();
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

	@Command(commandName = "virtualRecordItemClicked", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails")
			})
	CommandResult virtualRecordItemClicked(InputEvent event) {
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
			}
			return result;
		}
		return null;
	}


	@Command
	String handleVirtualRefresh(ExecutionStep step, InputEvent event, Object result) {
		if(event.getEventType().equals("TopRefresh")) {
			showSucRecords(true);
		} else if(event.getEventType().equals("BottomRefresh")) {
			sucessNoMoreDataAlert = false;
			registerBean("sucessNoMoreDataAlert", sucessNoMoreDataAlert);
			if(successTradeAccountListBean != null) {

				switch (step) {
				case PROCESS:
					if(successTradeAccountListBean.getData() != null && successTradeAccountListBean.getData().size()>0)
						oldDataSize  = successTradeAccountListBean.getData().size();
					
					successTradeAccountListBean.loadMoreData();
					break;
				case NAVIGATION:
					if(successTradeAccountListBean.getData() != null && successTradeAccountListBean.getData().size()>0) {
						newDataSize  = successTradeAccountListBean.getData().size();
					}
					
					if(newDataSize <= oldDataSize) {
						sucessNoMoreDataAlert = true;
						registerBean("sucessNoMoreDataAlert", sucessNoMoreDataAlert);
					} else {
						return null;
					}
					
				default:
					break;
				}
			
			}
		}
		return null;
	}
	
	
	@Command
	String handleActualRefresh(ExecutionStep step, InputEvent event, Object result) {
		if(event.getEventType().equals("TopRefresh")) {
			showAllRecords(true);
		} else if(event.getEventType().equals("BottomRefresh")) {
			allNoMoreDataAlert = false;
			registerBean("allNoMoreDataAlert", allNoMoreDataAlert);
			if(allTradeAccountListBean != null) {
				switch (step) {
				case PROCESS:
					if(allTradeAccountListBean.getData() != null && allTradeAccountListBean.getData().size()>0)
						oldDataSize = allTradeAccountListBean.getData().size();
					
					allTradeAccountListBean.loadMoreData();
					break;
				case NAVIGATION:
					if(allTradeAccountListBean.getData() != null && allTradeAccountListBean.getData().size()>0) {
						newDataSize = allTradeAccountListBean.getData().size();
					}
					
					if(newDataSize <= oldDataSize) {
						allNoMoreDataAlert = true;
						registerBean("allNoMoreDataAlert", allNoMoreDataAlert);
					} else {
						return null;
					}
					
				default:
					break;
				}
				//myChallengeListBean.loadMoreData();
			}
		}
		return null;
	}

	
	private void closeAlert() {
		allNoMoreDataAlert = false;
		sucessNoMoreDataAlert = false;
		
		registerBean("allNoMoreDataAlert", allNoMoreDataAlert);
		registerBean("sucessNoMoreDataAlert", sucessNoMoreDataAlert);
	}
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		allTradeAccountListBeanUpdater.doEvaluate();
		successTradeAccountListBeanUpdater.doEvaluate();
//		if(curItemId  == 1) {
//			if(tradingService != null) {
//				//int completeSize = successTradeAccountListBean.getData().size();
////				tradingService.getGain(0, 20);
//			}
//			virtualRecordsListField.getDomainModel().doEvaluate();
//			
//		} else if(curItemId == 2) {
//			if(tradingService != null) {
//			//	int completeSize = allTradeAccountListBean.getData().size();
////				tradingService.getTotalGain(0, 20);
//			}
//			actualRecordsListField.getDomainModel().doEvaluate();
//		}
		
		return null;
	}
	
	@OnHide
	void DestroyData() {
		closeAlert();
	}
}
