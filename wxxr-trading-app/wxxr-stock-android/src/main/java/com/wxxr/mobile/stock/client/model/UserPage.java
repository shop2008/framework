package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

/**
 * 个人主页
 * 
 * @author renwenjie
 */
@View(name = "userPage", withToolbar = true, description = "我的主页", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_page_layout")
public abstract class UserPage extends PageBase {

	
	/*@Bean(type=BindingType.Service)
	IMockDataService service;*/
	@Bean(type=BindingType.Service)
	IUserManagementService service;
	
	@Bean(type=BindingType.Pojo, express="${service.getMyUserInfo()}")
	UserBean userBean;
	
	@Bean(type=BindingType.Pojo, express="${service!=null?service.getMyPersonalHomePage(false):null}")
	PersonalHomePageBean personalHomePageBean;
	
	@Field(valueKey="options", binding="${personalHomePageBean!=null?personalHomePageBean.allist:null}", 
			attributes={
			@Attribute(name="joinShareCount", value="${personalHomePageBean!=null?personalHomePageBean.virtualCount:0}"), 
			@Attribute(name="challengeShareCount", value="${personalHomePageBean!=null?personalHomePageBean.actualCount:0}"),
			@Attribute(name="userHomeBackUri", value="${userBean!=null?userBean.homeBack:'resourceId:drawable/back1'}"),
			@Attribute(name="userIconUri", value="${userBean!=null?userBean.userPic:'resourceId:drawable/head4'}"),
			@Attribute(name="totalScoreProfit", value="${personalHomePageBean!=null?personalHomePageBean.voucherVol:0}"),
			@Attribute(name="totalMoneyProfit", value="${personalHomePageBean!=null?personalHomePageBean.totalProfit:0.0}")
	})
	List<GainBean> successTradeRecords;
	
	@Convertor(params = { @Parameter(name = "format", value = "%.0f"),
			@Parameter(name = "nullString", value = "0") })
	StockLong2StringConvertor shareNumConvertor;
	/*@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${loginMgr.myUserInfo}")
	UserBean user;

	@Bean(type=BindingType.Service)
	IUserLoginManagementService loginMgr;
	
	@Menu(items = { "left", "right" })
	private IMenu toolbar;

	@Bean(type = BindingType.Pojo, express = "${usrService.getMyPersonalHomePage(false)}")
	PersonalHomePageBean personalBean;
	*//**
	 * 用户形象照
	 *//*
	@Field(valueKey = "imageURI", binding = "${(user!=null&&user.userPic!=null)?user.userPic:'resourceId:drawable/head4'}")
	String userIcon;

	*//**
	 * 用户昵称
	 *//*
	@Field(valueKey = "text", binding = "${(user!=null&&user.nickName!=null)?user.nickName:'设置昵称'}", enableWhen="${(user!=null&&user.nickName!=null)?false:true}")
	String userNickName;

	*//**
	 * 累计实盘积分
	 *//*
	@Field(valueKey = "text", binding = "${personalBean!=null?personalBean.voucherVol:null}", converter = "scoreConvertor")
	String totalScore;

	*//**
	 * 累计总收益
	 *//*
	@Field(valueKey = "text", binding = "${personalBean!=null?personalBean.totalProfit:null}", converter = "profitConvertor")
	String totalProfit;

	*//**
	 * 挑战交易盘分享多少笔
	 *//*
	@Field(valueKey = "text", binding = "${personalBean!=null?personalBean.actualCount:null}", converter = "shareNumConvertor")
	String challengeSharedNum;

	*//**
	 * 参赛交易盘分享多少笔
	 *//*
	@Field(valueKey = "text", binding = "${personalBean!=null?personalBean.virtualCount:null}", converter = "shareNumConvertor")
	String joinSharedNum;

	@Field(valueKey = "options", binding = "${usrService.getMyPersonalHomePage(true).getVirtualList()}", upateAsync=true)
	List<GainBean> joinTradeInfos;

	@Field(valueKey = "options", binding = "${usrService.getMyPersonalHomePage(true).getActualList()}",upateAsync=true)
	List<GainBean> challengeTradeInfos;

	@Field(valueKey = "visible", binding = "${personalBean!=null?(personalBean.actualList!=null?(personalBean.actualList.size()>0?true:false):false):false}")
	boolean cSharedVisiable;

	@Field(valueKey = "visible", binding = "${personalBean!=null&&personalBean.actualList!=null&&personalBean.actualList.size()>0?false:true}")
	boolean cNoSharedVisiable;

	@Field(valueKey = "visible", binding = "${personalBean!=null?(personalBean.virtualList!=null?(personalBean.virtualList.size()>0?true:false):false):false}")
	boolean jSharedVisiable;

	@Field(valueKey = "visible", binding = "${personalBean!=null&&personalBean.virtualList!=null&&personalBean.virtualList.size()>0?false:true}")
	boolean jNoSharedVisiable;
	
	DataField<Boolean> jNoSharedVisiableField;

	@Field(valueKey = "backgroundImageURI", binding = "${user!=null?user.homeBack!=null?user.homeBack:'resourceId:drawable/back1':'resourceId:drawable/back1'}")
	String userHomeBack = "resourceId:drawable/back1";


	DataField<Boolean> cNoSharedVisiableField;
	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@Command(uiItems = { @UIItem(id = "right", label = "", icon = "resourceId:drawable/setting_small") }, navigations = { @Navigation(on = "OK", showPage = "userSelfDefine") })
	String toolbarClickedRight(InputEvent event) {
		return "OK";
	}

	@Convertor(params = { @Parameter(name = "format", value = "%.0f"),
			@Parameter(name = "nullString", value = "0") })
	StockLong2StringConvertor scoreConvertor;

	@Convertor(params = { @Parameter(name = "format", value = "%.2f"),
			@Parameter(name = "nullString", value = "0.00"),
			@Parameter(name = "multiple", value = "100.00f") })
	StockLong2StringConvertor profitConvertor;

	@Convertor(params = { @Parameter(name = "format", value = "%.0f"),
			@Parameter(name = "nullString", value = "0") })
	StockLong2StringConvertor shareNumConvertor;

	@OnHide
	void onHidePage() {
		loadingExpireTime = true;
	}

	boolean jNoSharedFlag = false;
	boolean cNoSharedFlag = false;
	
	@OnShow
	void initData(){
		jNoSharedFlag = false;
		cNoSharedFlag = false;
		final Runnable[] tasks = new Runnable[1];
		
		tasks[0] = new Runnable() {
			
			@Override
			public void run() {
				
				if(personalBean!=null && personalBean.getActualList()!=null&&personalBean.getActualList().size()>0) {
					cNoSharedVisiableField.setValue(false);
					cNoSharedFlag = true;
				}
				
				if(personalBean!=null && personalBean.getVirtualList()!=null&&personalBean.getVirtualList().size()> 0) {
					jNoSharedVisiableField.setValue(false);
					jNoSharedFlag = true;
				}
				
				if(jNoSharedFlag && cNoSharedFlag) {
					return;
				}
				AppUtils.runOnUIThread(tasks[0], 100, TimeUnit.MILLISECONDS);
			}
		};
		AppUtils.runOnUIThread(tasks[0], 0, TimeUnit.SECONDS);
	}
	*//**
	 * 挑战交易盘-"查看更多"事件处理
	 * 
	 * @param event
	 * @return
	 *//*
	@Command(commandName = "challengeViewMore", description = "To Challenge View More", navigations = { @Navigation(on = "OK", showPage = "userViewMorePage") })
	CommandResult challengeViewMore(InputEvent event) {

		CommandResult result = new CommandResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isVirtual", false);
		result.setPayload(map);
		result.setResult("OK");

		return result;
	}

	*//**
	 * 
	 * 参赛交易盘-"查看更多"事件处理
	 * 
	 * @param event
	 * @return
	 *//*
	@Command(commandName = "joinViewMore", description = "To Join View More", navigations = { @Navigation(on = "OK", showPage = "userViewMorePage") })
	CommandResult joinViewMore(InputEvent event) {
		CommandResult result = new CommandResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isVirtual", true);
		result.setPayload(map);
		result.setResult("OK");
		return result;
	}

	@Command(commandName = "joinItemClick", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "sellTradingAccount"),
			@Navigation(on = "BuyIn", showPage = "TBuyTradingPage") })
	CommandResult joinItemClick(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			GainBean virtualBean = null;

			// 本人
			if (personalBean != null) {
				List<GainBean> virtualList = personalBean.getVirtualList();
				if (virtualList != null && virtualList.size() > 0) {
					virtualBean = virtualList.get(position);
				}
			}
			CommandResult result = null;
			if (virtualBean != null) {
				*//** 交易盘ID *//*
				Long accId = virtualBean.getTradingAccountId();
				String tradeStatus = virtualBean.getOver();
				Boolean isVirtual = virtualBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accid", accId);
				map.put("isVirtual", isVirtual);
				map.put("isSelf", true);
				result.setPayload(map);
				if ("CLOSED".equals(tradeStatus)) {
					result.setResult("operationDetails");

				} else if ("UNCLOSE".equals(tradeStatus)) {
					int status = virtualBean.getStatus();
					if (status == 0) {
						// 进入卖出界面
						result.setResult("SellOut");
					} else if (status == 1) {
						// 进入买入界面
						result.setResult("BuyIn");
					}
				}
				updateSelection(new AccidSelection(String.valueOf(accId),
						isVirtual));
			}
			return result;
		}
		return null;
	}

	@Command(commandName = "challengeItemClick", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "sellTradingAccount"),
			@Navigation(on = "BuyIn", showPage = "TBuyTradingPage") })
	CommandResult challengeItemClick(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			GainBean actualBean = null;

			// 本人
			if (personalBean != null) {
				List<GainBean> actualList = personalBean.getActualList();
				if (actualList != null && actualList.size() > 0) {
					actualBean = actualList.get(position);
				}
			}
			CommandResult result = null;
			if (actualBean != null) {
				*//** 交易盘ID *//*
				Long accId = actualBean.getTradingAccountId();
				String tradeStatus = actualBean.getOver();
				Boolean isVirtual = actualBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accid", accId);
				map.put("isVirtual", isVirtual);
				map.put("isSelf", true);
				result.setPayload(map);
				if ("CLOSED".equals(tradeStatus)) {
					result.setResult("operationDetails");
				} else if ("UNCLOSE".equals(tradeStatus)) {
					int status = actualBean.getStatus();
					if (status == 0) {
						// 进入卖出界面
						result.setResult("SellOut");
					} else if (status == 1) {
						// 进入买入界面
						result.setResult("BuyIn");
					}
				}
				updateSelection(new AccidSelection(String.valueOf(accId),
						isVirtual));
			}
			return result;
		}
		return null;
	}

	*//**
	 * 设置昵称
	 * 
	 * @param event
	 * @return
	 *//*
	@Command(commandName = "setNickName", description = "To Personal_setting UI", navigations = { @Navigation(on = "SUCCESS", showPage = "userNickSet") })
	String setNickName(InputEvent event) {

		return "SUCCESS";
	}
	
	@Command
	String handlerReTryClicked(InputEvent event) {
		if(usrService!=null) {
			usrService.getMyPersonalHomePage(false);
		}
		return null;
	}*/
	
	@Command(commandName = "handleTradeRecordItemClick", navigations = {
			@Navigation(on = "operationDetails", showPage = "OperationDetails"),
			@Navigation(on = "SellOut", showPage = "sellTradingAccount"),
			@Navigation(on = "BuyIn", showPage = "TBuyTradingPage") })
	CommandResult handleTradeRecordItemClick(InputEvent event) {
		
		int position = (Integer) event.getProperty("position");
		List<GainBean> allList = null;
		allList = personalHomePageBean.getAllist();
		
		List<GainBean> virtualList = null;
		List<GainBean> actualList = null;
		
		GainBean actualBean = null;
		if(allList != null && allList.size()>0) {
			virtualList = new ArrayList<GainBean>();
			actualList = new ArrayList<GainBean>();
			
			for(GainBean vo: allList) {
				if(vo.getVirtual()) {
					virtualList.add(vo);
				} else {
					actualList.add(vo);
				}
			}
		}
		Comparator<GainBean> comparator = new Comparator<GainBean>() {

			@Override
			public int compare(final GainBean lhs, final GainBean rhs) {
				
				Long lh = null;
				Long rh = null;
				if(lhs != null) {
					lh = lhs.getTradingAccountId();
				}
				
				if(rhs != null) {
					rh = rhs.getTradingAccountId();
				}
				
				if(lh !=null && rh !=null) {
					return rh > lh? 1:-1;
				}
				return 0;
			}
		};
		List<GainBean> sortList = new ArrayList<GainBean>();
		if(virtualList!=null && virtualList.size()>0) {
			Collections.sort(virtualList, comparator);
			sortList.addAll(virtualList);
		}
		
		if(actualList!=null && actualList.size()>0) {
			Collections.sort(actualList, comparator);
			sortList.addAll(actualList);
		}
		
		actualBean = sortList.get(position);
		CommandResult result = null;	
		if (actualBean != null) {
			
			Long accId = actualBean.getTradingAccountId();
			String tradeStatus = actualBean.getOver();
			Boolean isVirtual = actualBean.getVirtual();
			result = new CommandResult();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("accid", accId);
			map.put("isVirtual", isVirtual);
			map.put("isSelf", true);
			result.setPayload(map);
			if ("CLOSED".equals(tradeStatus)) {
				result.setResult("operationDetails");
			} else if ("UNCLOSE".equals(tradeStatus)) {
				int status = actualBean.getStatus();
				if (status == 0) {
					// 进入卖出界面
					result.setResult("SellOut");
				} else if (status == 1) {
					// 进入买入界面
					result.setResult("BuyIn");
				}
			}
			updateSelection(new AccidSelection(String.valueOf(accId),
					isVirtual));
			
			return result;
		}
		
		return null;
	}
}
