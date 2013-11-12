package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.text.TextUtils;

import com.wxxr.javax.ws.rs.NameBinding;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageCallback;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.bean.TradingAccount;
import com.wxxr.mobile.stock.client.bean.User;
import com.wxxr.mobile.stock.client.service.IUserManagementService;
import com.wxxr.mobile.stock.client.utils.ColorUtils;

/**
 * 个人主页
 * 
 * @author renwenjie
 */
@View(name = "user_page")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_page_layout")
public abstract class UserPage extends PageBase implements IModelUpdater {

	/**
	 * 用户形象照
	 */
	@Field(valueKey = "imageURI")
	String userIcon;

	/**
	 * 用户昵称
	 */
	@Field(valueKey = "text")
	String userNickName;

	/**
	 * 累计实盘积分
	 */
	@Field(valueKey = "text")
	String totalScore;

	/**
	 * 累计总收益
	 */
	@Field(valueKey = "text")
	String totalProfit;

	/**
	 * 挑战交易盘分享多少笔
	 */
	@Field(valueKey = "text")
	String challengeSharedNum;

	/**
	 * 参赛交易盘分享多少笔
	 */
	@Field(valueKey = "text")
	String joinSharedNum;

	@Field(valueKey = "options")
	List<TradingAccount> joinTradeInfos;

	@Field(valueKey = "options")
	List<TradingAccount> challengeTradeInfos;

	@Field(valueKey = "visible")
	boolean cSharedVisiable;

	@Field(valueKey = "visible")
	boolean cNoSharedVisiable;

	@Field(valueKey = "visible")
	boolean jSharedVisiable;

	@Field(valueKey = "visible")
	boolean jNoSharedVisiable;

	@Field
	String alteredUserIcon = null;

	@Field
	String alteredUserHome = null;

	@Field(valueKey = "backgroundImageURI")
	String userHomeBack;

	DataField<String> userHomeBackField;
	DataField<String> userIconField;

	DataField<String> userNickNameField;

	DataField<String> totalScoreField;

	DataField<String> totalProfitField;

	DataField<String> challengeSharedNumField;

	DataField<String> joinSharedNumField;
	DataField<List> challengeTradeInfosField;
	DataField<List> joinTradeInfosField;

	DataField<Boolean> cSharedVisiableField;
	DataField<Boolean> cNoSharedVisiableField;
	DataField<Boolean> jSharedVisiableField;
	DataField<Boolean> jNoSharedVisiableField;

	@Field
	User user;

	@OnShow
	protected void initData() {

		user = getUIContext().getKernelContext()
				.getService(IUserManagementService.class).fetchUserInfo();
		if (user != null) {
			List<TradingAccount> tradeInfos = user.getTradeInfos();
			joinTradeInfos = new ArrayList<TradingAccount>();
			challengeTradeInfos = new ArrayList<TradingAccount>();
			for (TradingAccount tradeInfo : tradeInfos) {
				switch (tradeInfo.getType()) {
				case 0:
					/* 参赛交易盘 */
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
		setUserHomeBack(user.getHomeBack());
		setTotalProfit(user.getTotoalProfit());
		setTotoalScore(user.getTotoalScore());
		setChallengeSharedNum(user.getChallengeShared());
		setJoinSharedNum(user.getJoinShared());
	}

	/**
	 * 设置用户的背景图片
	 * 
	 * @param homeBack
	 */
	private void setUserHomeBack(String homeBack) {
		if (TextUtils.isEmpty(homeBack)) {
			this.userHomeBack = "resourceId:drawable/back1";
			this.userHomeBackField.setValue("resourceId:drawable/back1");
		} else {
			this.userHomeBack = homeBack;
			this.userHomeBackField.setValue(homeBack);
		}
	}

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
	@Command(commandName = "manage", description = "To Manage UI")
	String manage(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {

			System.out.println("-----manage-----");

		}
		return null;
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
							on = "", 
							showPage = "userSelfDefine",
							params = { 
									@Parameter(name = "curUserIcon", value = "$user.userIcon"),
									@Parameter(name = "curUserHomeBack", value = "$user.userHomeBack")
									}
							) 
					}
			)
	String personalSet(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			//System.out.println("123");

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("curUserIcon", this.userIcon);
			map.put("curUserHomeBack", this.userHomeBack);
			getUIContext()
					.getWorkbenchManager()
					.getPageNavigator()
					.showPage(
							getUIContext().getWorkbenchManager().getWorkbench()
									.getPage("userSelfDefine"), map,
							new IPageCallback() {

								@Override
								public void onShow(IPage page) {

								}

								@Override
								public void onHide(IPage page) {

									// 处理回调 
									alteredUserIcon = page
											.getAttribute(AttributeKeys.text);
									if (alteredUserIcon != null) {

										userIcon = alteredUserIcon;
										userIconField.setValue(alteredUserIcon);
									}

									alteredUserHome = page
											.getAttribute(AttributeKeys.name);

									if (alteredUserHome != null) {

										userHomeBack = alteredUserHome;
										userHomeBackField
												.setValue(alteredUserHome);
									}
								}

								@Override
								public void onDestroy(IPage page) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onCreate(IPage page) {
									// TODO Auto-generated method stub

								}
							});
		}
		return null;
	}

	class MyPageCallBack implements IPageCallback {

		@Override
		public void onCreate(IPage page) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onShow(IPage page) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onHide(IPage page) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onDestroy(IPage page) {
			// TODO Auto-generated method stub

		}
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
	 * 
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

	protected void setUserIcon(String uIcon) {
		this.userIcon = uIcon;
		this.userIconField.setValue(uIcon);
	}

	protected void setUserNickName(String uNickName) {
		this.userNickName = uNickName;
		this.userNickNameField.setValue(uNickName);
	}

	protected void setTotoalScore(String totalScore) {
		this.totalScore = totalScore;
		this.totalScoreField.setValue(totalScore);

		if (Float.parseFloat(totalScore) > 0) {
			this.totalScoreField.setAttribute(AttributeKeys.textColor,
					ColorUtils.STOCK_RED);
		} else {
			this.totalScoreField.setAttribute(AttributeKeys.textColor,
					ColorUtils.STOCK_GREEN);
		}
	}

	protected void setTotalProfit(String totalProfit) {
		this.totalProfit = totalProfit;

		this.totalProfitField.setValue(totalProfit);
		if (Float.parseFloat(totalProfit) > 0) {
			this.totalProfitField.setAttribute(AttributeKeys.textColor,
					ColorUtils.STOCK_RED);
		} else {
			this.totalProfitField.setAttribute(AttributeKeys.textColor,
					ColorUtils.STOCK_GREEN);
		}
	}

	protected void setChallengeSharedNum(String num) {
		this.challengeSharedNum = num;
		this.challengeSharedNumField.setValue(num);

		if (Integer.parseInt(num) > 0) {
			this.cSharedVisiable = true;
			this.cNoSharedVisiable = false;

			this.cSharedVisiableField.setValue(true);
			this.cNoSharedVisiableField.setValue(false);
		} else {
			this.cSharedVisiable = false;
			this.cNoSharedVisiable = true;

			this.cSharedVisiableField.setValue(false);
			this.cNoSharedVisiableField.setValue(true);
		}
	}

	protected void setJoinSharedNum(String num) {
		this.joinSharedNum = num;
		this.joinSharedNumField.setValue(num);

		if (Integer.parseInt(num) > 0) {
			this.jSharedVisiable = true;
			this.jNoSharedVisiable = false;

			this.jSharedVisiableField.setValue(true);
			this.jNoSharedVisiableField.setValue(false);
		} else {
			this.jSharedVisiable = false;
			this.jNoSharedVisiable = true;

			this.jSharedVisiableField.setValue(false);
			this.jNoSharedVisiableField.setValue(true);
		}

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
	@Command(commandName = "setNickName")
	String setNickName(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getWorkbench()
					.showPage("user_nick_set", null, null);
		}
		return null;
	}

	@Override
	public void updateModel(Object value) {

	}
}
