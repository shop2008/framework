package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.text.TextUtils;

import com.wxxr.javax.ws.rs.NameBinding;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageCallback;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.utils.ColorUtils;

/**
 * 个人主页
 * 
 * @author renwenjie
 */
@View(name = "userPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_page_layout")
public abstract class UserPage extends PageBase implements IModelUpdater {

	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type=BindingType.Pojo,express="${usrService.fetchUserInfo}")
	UserBean user;
	
	/**
	 * 用户形象照
	 */
	@Field(valueKey = "imageURI", binding="${user!=null?user.userPic:null}")
	String userIcon;

	/**
	 * 用户昵称
	 */
	@Field(valueKey = "text", binding="${user!=null?user.homeBack:null}")
	String userNickName;

	/**
	 * 累计实盘积分
	 */
	@Field(valueKey = "text", binding="${user!=null?user.totoalScore:null}")
	String totalScore;

	/**
	 * 累计总收益
	 */
	@Field(valueKey = "text", binding="${user!=null?user.totoalProfit:null}")
	String totalProfit;

	/**
	 * 挑战交易盘分享多少笔
	 */
	@Field(valueKey = "text", binding="${user!=null?user.challengeShared:null}")
	String challengeSharedNum;

	/**
	 * 参赛交易盘分享多少笔
	 */
	@Field(valueKey = "text", binding="${user!=null?user.joinShared:null}")
	String joinSharedNum;

	@Field(valueKey = "options", binding="${user!=null?user.joinTradeInfos:null}")
	List<TradingAccountBean> joinTradeInfos;

	@Field(valueKey = "options", binding="${user!=null?user.challengeTradeInfos:null}")
	List<TradingAccountBean> challengeTradeInfos;

	@Field(valueKey = "visible", binding="${(user!=null&&user.challengeTradeInfos.size>0) ? true:false}")
	boolean cSharedVisiable;

	@Field(valueKey = "visible", binding="${(user!=null&&user.challengeTradeInfos.size>0) ? false:true}")
	boolean cNoSharedVisiable;

	@Field(valueKey = "visible", binding="${(user!=null&&user.joinTradeInfos.size>0) ? true:false}")
	boolean jSharedVisiable;

	@Field(valueKey = "visible", binding="${(user!=null&&user.joinTradeInfos.size>0) ? false:false}")
	boolean jNoSharedVisiable;



	@Field(valueKey = "backgroundImageURI", binding="${user!=null?user.homeBack:null}")
	String userHomeBack;

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
							on = "SUCCESS", 
							showPage = "userSelfDefine",
							params = { 
									@Parameter(name = "curUserIcon", value = "${user.userIcon}"),
									@Parameter(name = "curUserHomeBack", value = "${user.userHomeBack}")
									}
							) 
					}
			)
	String personalSet(InputEvent event) {
		return "SUCCESS";
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

			ApplicationFactory.getInstance().getApplication();

			System.out.println((Integer) event.getProperty("position"));
		}
		return null;
	}

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

	@Override
	public void updateModel(Object value) {
		
		
	}
}
