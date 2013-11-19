package com.wxxr.mobile.stock.client.model;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="otherUserPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY,layoutId="R.layout.other_user_page_layout")
public abstract class OtherUserPage extends PageBase implements IModelUpdater {
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type=BindingType.Pojo,express="${usrService.getUserInfoById(userId)}")
	UserBean user;
	
	@Field(valueKey="text", binding="${usrService!=null}")
	String otherUserPageTitle;
	
	
	@Field(valueKey = "backgroundImageURI", binding="${user!=null?user.homeBack!=null?user.homeBack:null:null}")
	String userHomeBack;
	
	/**用户头像*/
	@Field(valueKey = "imageURI", binding="${user!=null?user.userPic!=null?user.userPic:null:null}")
	String userIcon;
	
	/**
	 * 用户昵称
	 */
	@Field(valueKey = "text", binding="${user!=null?user.nickName!=null?user.nickName:'--':'--'}")
	String userNickName;

	/**
	 * 累计实盘积分
	 */
	@Field(valueKey = "text", binding="${user!=null?user.totoalScore!=null?user.totoalScore:'--':'--'}")
	String totalScore;

	/**
	 * 累计总收益
	 */
	@Field(valueKey = "text", binding="${user!=null?user.totoalProfit!=null?user.totoalProfit:'--':'--'}")
	String totalProfit;

	/**
	 * 挑战交易盘分享多少笔
	 */
	@Field(valueKey = "text", binding="${user!=null?user.challengeShared!=null?user.challengeShared:'--':'--'}")
	String challengeSharedNum;

	/**
	 * 参赛交易盘分享多少笔
	 */
	@Field(valueKey = "text", binding="${user!=null?user.joinShared!=null?user.joinShared:'--':'--'}")
	String joinSharedNum;
	
	@Field(valueKey = "options", binding="${tradingAccount.virtualTradingAccountBeans!=null?tradingAccount.virtualTradingAccountBeans:null}")
	List<TradingAccountBean> joinTradeInfos;

	@Field(valueKey = "options", binding="${tradingAccount.realTradingAccountBeans!=null?tradingAccount.realTradingAccountBeans:null}")
	List<TradingAccountBean> challengeTradeInfos;

	@Field(valueKey = "visible", binding="${tradingAccount.realTradingAccountBeans!=null?true:false}")
	boolean cSharedVisiable;

	@Field(valueKey = "visible", binding="${tradingAccount.realTradingAccountBeans!=null?false:true}")
	boolean cNoSharedVisiable;

	@Field(valueKey = "visible", binding="${tradingAccount.virtualTradingAccountBeans!=null?true:false}")
	boolean jSharedVisiable;

	@Field(valueKey = "visible", binding="${tradingAccount.virtualTradingAccountBeans!=null?false:true}")
	boolean jNoSharedVisiable;
	
	/**其他人用户ID*/
	@Bean
	String userId;
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

	
	
	@Override
	public void updateModel(Object value) {
		Map<String, String> map = (Map<String, String>) value;
		userId = map.get("userId");
		registerBean("userId", userId);
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
}
