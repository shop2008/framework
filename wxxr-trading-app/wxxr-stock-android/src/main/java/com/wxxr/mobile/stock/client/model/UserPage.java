package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.utils.Float2StringConvertor;

/**
 * 个人主页
 * 
 * @author renwenjie
 */
@View(name = "userPage", withToolbar=true, description="我的主页")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_page_layout")
public abstract class UserPage extends PageBase  {

	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	

	
	@Bean(type=BindingType.Pojo,express="${usrService.myUserInfo}")
	UserBean user;
	
	@Menu(items={"left","right"})
	private IMenu toolbar;
	

	@Bean(type=BindingType.Pojo, express="${usrService.myPersonalHomePage}")
	PersonalHomePageBean personalBean;
	/**
	 * 用户形象照
	 */
	@Field(valueKey = "imageURI", binding="${user!=null?user.userPic!=null?user.userPic:'resourceId:drawable/head1':'resourceId:drawable/head1'}")
	String userIcon = "resourceId:drawable/head1";

	/**
	 * 用户昵称
	 */
	@Field(valueKey = "text", binding="${user!=null?user.nickName!=null?user.nickName:'--':'--'}")
	String userNickName;

	/**
	 * 累计实盘积分
	 */
	@Field(valueKey = "text", binding="${personalBean!=null?personalBean.voucherVol>=0?personalBean.voucherVol:'--':'--'}")
	String totalScore;

	/**
	 * 累计总收益
	 */
	@Field(valueKey = "text", binding="${personalBean!=null?personalBean.totalProfit>=0?personalBean.totalProfit:'--':'--'}", converter="f2SConvertor")
	String totalProfit;

	/**
	 * 挑战交易盘分享多少笔
	 */
	@Field(valueKey = "text", binding="${personalBean!=null?personalBean.actualCount>=0?personalBean.actualCount:'--':'--'}")
	String challengeSharedNum;

	/**
	 * 参赛交易盘分享多少笔
	 */
	@Field(valueKey = "text", binding="${personalBean!=null?personalBean.virtualCount>=0?personalBean.virtualCount:'--':'--'}")
	String joinSharedNum;

	@Field(valueKey = "options", binding="${personalBean!=null?personalBean.virtualList!=null?personalBean.virtualList:null:null}")
	List<GainBean> joinTradeInfos;

	@Field(valueKey = "options", binding="${personalBean!=null?personalBean.actualList!=null?personalBean.actualList:null:null}")
	List<GainBean> challengeTradeInfos;

	@Field(valueKey = "visible", binding="${personalBean.actualList!=null?true:false}")
	boolean cSharedVisiable;

	@Field(valueKey = "visible", binding="${personalBean.actualList!=null?false:true}")
	boolean cNoSharedVisiable;

	@Field(valueKey = "visible", binding="${personalBean.virtualList!=null?true:false}")
	boolean jSharedVisiable;

	@Field(valueKey = "visible", binding="${personalBean.virtualList!=null?false:true}")
	boolean jNoSharedVisiable;

	@Field(valueKey = "backgroundImageURI", binding="${user!=null?user.homeBack!=null?user.homeBack:'resourceId:drawable/back1':'resourceId:drawable/back1'}")
	String userHomeBack = "resourceId:drawable/back1";

	
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);	
		return null;
	}
	
	
	@Command(
			uiItems={
				@UIItem(id="right",label="",icon="resourceId:drawable/setting")
			},
			navigations={@Navigation(on="OK", showPage="userSelfDefine")}
	)
	String toolbarClickedRight(InputEvent event){
		return "OK";
	}
	
	@Convertor(
			params={@Parameter(name="format", value="%10.2f")}
			)
	Float2StringConvertor f2SConvertor;


	/**
	 * 挑战交易盘-"查看更多"事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(
			commandName = "challengeViewMore", 
			description = "To Challenge View More",
			navigations={@Navigation(on="OK", showPage="userViewMorePage")}
			)
	CommandResult challengeViewMore(InputEvent event) {

		CommandResult result = new CommandResult();
		Map<String, String> map = new HashMap<String, String>();
		map.put("isVirtual", "0");
		result.setPayload(map);
		result.setResult("OK");
		
		return result;
	}

	/**
	 * 
	 * 参赛交易盘-"查看更多"事件处理
	 * @param event
	 * @return
	 */
	@Command(
			commandName = "joinViewMore", 
			description = "To Join View More",
			navigations={@Navigation(on="OK", showPage="userViewMorePage")}
			)
	CommandResult joinViewMore(InputEvent event) {
		CommandResult result = new CommandResult();
		Map<String, String> map = new HashMap<String, String>();
		map.put("isVirtual", "1");
		result.setPayload(map);
		result.setResult("OK");
		return result;
	}


	@Command(commandName = "joinItemClick",navigations={
			@Navigation(on="operationDetails",showPage="OperationDetails"),
			@Navigation(on="SellOut",showPage="sellTradingAccount"),
			@Navigation(on="BuyIn",showPage="TBuyTradingPage")
			})
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
				/** 交易盘ID */
				Long accId = virtualBean.getTradingAccountId();
				String tradeStatus = virtualBean.getOver();
				Boolean isVirtual = virtualBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accid", accId);
				map.put("isVirtual", isVirtual);
				result.setPayload(map);
				if ("CLOSED".equals(tradeStatus)) {
					result.setResult("operationDetails");
				}
				if ("UNCLOSE".equals(tradeStatus)) {
					int status = virtualBean.getStatus();
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

	@Command(commandName = "challengeItemClick",navigations={
			@Navigation(on="operationDetails",showPage="OperationDetails"),
			@Navigation(on="SellOut",showPage="sellTradingAccount"),
			@Navigation(on="BuyIn",showPage="TBuyTradingPage")
			})
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
				/** 交易盘ID */
				Long accId = actualBean.getTradingAccountId();
				String tradeStatus = actualBean.getOver();
				Boolean isVirtual = actualBean.getVirtual();
				result = new CommandResult();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("accid", accId);
				map.put("isVirtual", isVirtual);
				result.setPayload(map);
				if ("CLOSED".equals(tradeStatus)) {
					result.setResult("operationDetails");
				}
				if ("UNCLOSE".equals(tradeStatus)) {
					int status = actualBean.getStatus();
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

	
/*	protected void registerSelectionListener(){
		getUIContext().getWorkbenchManager().getWorkbench().getSelectionService().addSelectionListener("userSelfDefine",new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(String providerId, ISelection selection) {
				
			}
		});
	}*/
	
	/**
	 * 设置昵称
	 * 
	 * @param event
	 * @return
	 */
	@Command(
			commandName = "setNickName", 
			description = "To Personal_setting UI", 
			navigations = { 
					@Navigation(
							on = "SUCCESS", 
							showPage = "userNickSet"
					)
			}
	)
	String setNickName(InputEvent event) {
		
		return "SUCCESS";
	}
}
