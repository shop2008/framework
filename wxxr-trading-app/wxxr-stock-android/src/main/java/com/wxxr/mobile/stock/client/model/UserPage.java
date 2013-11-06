package com.wxxr.mobile.stock.client.model;


import java.util.ArrayList;
import java.util.List;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.bean.TradingAccount;
import com.wxxr.mobile.stock.client.bean.User;
import com.wxxr.mobile.stock.client.service.IUserManagementService;

/**
 * 个人主页
 * @author renwenjie
 */
@View(name="user_page")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_page_layout")
public abstract class UserPage extends PageBase {

	/**
	 * 用户形象照
	 */
	@Field(valueKey="imageURI")
	String user_icon;
	
	/**
	 * 用户昵称
	 */
	@Field(valueKey="text")
	String user_nick_name;
	
	/**
	 * 累计实盘积分
	 */
	@Field(valueKey="text")
	String total_real_score;
	
	/**
	 * 累计总收益
	 */
	@Field(valueKey="text")
	String total_profit;
	
	/**
	 * 挑战交易盘分享多少笔
	 */
	@Field(valueKey="text")
	String challenge_shared_num_what;
	
	/**
	 * 参赛交易盘分享多少笔
	 */
	@Field(valueKey="text")
	String join_shared_num_what;
	
	@Field(valueKey="options")
	List<TradingAccount> joinTradeInfos;
	
	@Field(valueKey="options")
	List<TradingAccount> challengeTradeInfos;

	
	DataField<String> user_iconField;
	
	DataField<String> user_nick_nameField;
	
	DataField<String> total_real_scoreField;
	
	DataField<String> total_profitField;
	
	DataField<String> challenge_shared_num_whatField;
	
	DataField<String> join_shared_num_whatField;
	DataField<List> challengeTradeInfosField;
	DataField<List> joinTradeInfosField;
	
	@Field
	User user;
	@OnShow
	protected void initData() {
		user = getUIContext().getKernelContext().getService(IUserManagementService.class).fetchUserInfo();
	
		if (user != null) {
			List<TradingAccount> tradeInfos = user.getTradeInfos();
			joinTradeInfos = new ArrayList<TradingAccount>();
			challengeTradeInfos = new ArrayList<TradingAccount>();
			for (TradingAccount tradeInfo : tradeInfos) {
				switch (tradeInfo.getType()) {
				case 0:
					/*参赛交易盘*/
					joinTradeInfos.add(tradeInfo);
					break;
				case 1:
					challengeTradeInfos.add(tradeInfo);
					break;
				default:
					break;
				}
			}
			joinTradeInfosField.setValue(joinTradeInfos);
			challengeTradeInfosField.setValue(challengeTradeInfos);
			showView();
		}
	}
	
	
	
	
	protected void showView() {
		setUserIcon(user.getUserPic());
		setUserNickName(user.getNickName());
		setTotalProfit(user.getTotoalProfit());
		setTotoalScore(user.getTotoalScore());
		setChallengeSharedNum(user.getChallengeShared());
		setJoinSharedNum(user.getJoinShared());
	}

	/**
	 * 标题栏-"返回"按钮事件处理
	 * @param event
	 * @return
	 */
	@Command(commandName="back",description="Back To Last UI")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			System.out.println("-----back-----");
		}
		return null;
	}
	
	
	/**
	 * 标题栏-"管理"按钮事件处理
	 * @param event
	 * @return
	 */
	@Command(commandName="manage", description="To Manage UI")
	String manage(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			System.out.println("-----manage-----");
		}
		return null;
	}
	
	
	/**
	 * "个性化"按钮事件处理
	 * @param event
	 * @return
	 */
	@Command(commandName="personal_setting", description="To Personal_setting UI")
	String personal_setting(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			System.out.println("-----personal_setting-----");
		}
		return null;
	}
	
	
	/**
	 * 挑战交易盘-"查看更多"事件处理
	 * @param event
	 * @return
	 */
	@Command(commandName="challenge_view_more", description="To Challenge View More")
	String challenge_view_more(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			System.out.println("-----challenge_view_more-----");
		}
		return null;
	}
	
	/**
	 * 参赛交易盘-"查看更多"事件处理
	 * @param event
	 * @return
	 */
	@Command(commandName="join_view_more", description="To Join View More")
	String join_view_more(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			System.out.println("-----join_view_more-----");
		}
		return null;
	}
	
	protected void setUserIcon(String uIcon) {
		this.user_icon = uIcon;
		this.user_iconField.setValue(uIcon);
	}
	
	protected void setUserNickName(String uNickName) {
		this.user_nick_name = uNickName;
		this.user_nick_nameField.setValue(uNickName);
	}
	
	protected void setTotoalScore(String totalScore) {
		this.total_real_score = totalScore;
		this.total_real_scoreField.setValue(totalScore);
		
		if (Float.parseFloat(totalScore) > 0) {
			this.total_real_scoreField.setAttribute(AttributeKeys.foregroundColor, R.color.red);
		} else {
			this.total_real_scoreField.setAttribute(AttributeKeys.foregroundColor, R.color.green);
		}
	}
	
	protected void setTotalProfit(String totalProfit) {
		this.total_profit = totalProfit;
		
		this.total_profitField.setValue(totalProfit);
		if (Float.parseFloat(totalProfit) > 0) {
			this.total_profitField.setAttribute(AttributeKeys.foregroundColor, R.color.red);
		} else {
			this.total_profitField.setAttribute(AttributeKeys.foregroundColor, R.color.green);
		}
	}
	
	
	protected void setChallengeSharedNum(String num) {
		this.challenge_shared_num_what = num;
		this.challenge_shared_num_whatField.setValue(num);
	}
	
	protected void setJoinSharedNum(String num) {
		this.join_shared_num_what = num;
		this.join_shared_num_whatField.setValue(num);
	}
	
	@Command(commandName="join_item_click")
	String join_item_click(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			System.out.println((Integer)event.getProperty("position"));
		}
		return null;
	}
	
	@Command(commandName="challenge_item_click")
	String challenge_item_click(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			
			ApplicationFactory.getInstance().getApplication();
			
			System.out.println((Integer)event.getProperty("position"));
		}
		return null;
	}
}
