package com.wxxr.mobile.stock.client.model;

import java.util.List;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

/**
 * 个人主页
 * 
 * @author renwenjie
 */
@View(name = "userPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_page_layout")
public abstract class UserPage extends PageBase  {

	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	

	
	@Bean(type=BindingType.Pojo,express="${usrService.myUserInfo}")
	UserBean user;
	
	
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
	@Field(valueKey = "text", binding="${personalBean!=null?personalBean.totalProfit>=0?personalBean.totalProfit:'--':'--'}")
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

	/**
	 * 标题栏-"管理"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(
			commandName = "manage", 
			description = "To Manage UI", 
			navigations = { 
					@Navigation(
							on = "SUCCESS", 
							showPage = "userManagePage"
							) 
					}
			)
	String manage(InputEvent event) {
		return "SUCCESS";
	}

	/**
	 * "个性化"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(
			commandName = "personalSet", 
			description = "To Personal_setting UI", 
		
			navigations = { 
					@Navigation(
							on = "OK", 
							showPage = "userSelfDefine"
							) 
					}
			)
	String personalSet(InputEvent event) {
		return "OK";
	}

	/**
	 * 挑战交易盘-"查看更多"事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "challengeViewMore", description = "To Challenge View More")
	String challengeViewMore(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			System.out.println("-----challenge_view_more-----");

		}
		return null;
	}

	/**
	 * 
	 * 参赛交易盘-"查看更多"事件处理
	 * @param event
	 * @return
	 */
	@Command(commandName = "joinViewMore", description = "To Join View More")
	String joinViewMore(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			System.out.println("-----join_view_more-----");
		}
		return null;
	}


	@Command(commandName = "joinItemClick")
	String joinItemClick(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			System.out.println((Integer) event.getProperty("position"));
		}
		return null;
	}

	@Command(commandName = "challengeItemClick")
	String challengeItemClick(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			System.out.println((Integer) event.getProperty("position"));
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
