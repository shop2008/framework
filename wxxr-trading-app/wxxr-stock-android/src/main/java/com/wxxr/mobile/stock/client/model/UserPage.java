package com.wxxr.mobile.stock.client.model;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.android.ui.binding.GenericListAdapter;
import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.api.IApplication;
import com.wxxr.mobile.core.api.IProgressMonitor;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDataChanged;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.bean.TradeRecordEntity;
import com.wxxr.mobile.stock.client.bean.UserInfoEntity;

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
	String total_real_integral;
	
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
	
	@Field
	UserInfoEntity entity;

	@Field(valueKey="options")
	List<TradeRecordEntity> join_entities;
	
	@Field(valueKey="options")
	List<TradeRecordEntity> challenge_entities;

	
	DataField<String> user_iconField;
	
	DataField<String> user_nick_nameField;
	
	DataField<String> total_real_integralField;
	
	DataField<String> total_profitField;
	
	DataField<String> challenge_shared_num_whatField;
	
	DataField<String> join_shared_num_whatField;
	DataField<List> challenge_entitiesField;
	DataField<List> join_entitiesField;
	
	@OnCreate
	protected void initData() {
		entity = new UserInfoEntity();
		entity.setuIcon("resourceId:drawable/home");
		entity.setuNickName("李四");
		entity.setuChallengeShared("20");
		entity.setuJoinShared("23");
		entity.setuIntegrals("15000.00");
		entity.setuProfits("20000.00");
		
		
		List<TradeRecordEntity> tradeRecordEntities = new ArrayList<TradeRecordEntity>();
		
		TradeRecordEntity tradeRecordEntity1 = new TradeRecordEntity();
		tradeRecordEntity1.setStockName("工商银行");
		tradeRecordEntity1.setStockCode("006003");
		tradeRecordEntity1.setTradeAmount("7万");
		tradeRecordEntity1.setTradeDate("2013-11-05");
		tradeRecordEntity1.setTradeProfit("+6000.00");
		tradeRecordEntity1.setTradeType("challenge");
		
		tradeRecordEntities.add(tradeRecordEntity1);
		
		TradeRecordEntity tradeRecordEntity2 = new TradeRecordEntity();
		tradeRecordEntity2.setStockName("北京银行");
		tradeRecordEntity2.setStockCode("006002");
		tradeRecordEntity2.setTradeAmount("10万");
		tradeRecordEntity2.setTradeDate("2013-11-06");
		tradeRecordEntity2.setTradeProfit("+5000.00");
		tradeRecordEntity2.setTradeType("challenge");
		
		tradeRecordEntities.add(tradeRecordEntity2);
		
		
		TradeRecordEntity tradeRecordEntity5 = new TradeRecordEntity();
		tradeRecordEntity5.setStockName("北京银行");
		tradeRecordEntity5.setStockCode("006002");
		tradeRecordEntity5.setTradeAmount("10万");
		tradeRecordEntity5.setTradeDate("2013-11-06");
		tradeRecordEntity5.setTradeProfit("+5000.00");
		tradeRecordEntity5.setTradeType("challenge");
		
		tradeRecordEntities.add(tradeRecordEntity5);
		
		TradeRecordEntity tradeRecordEntity3 = new TradeRecordEntity();
		tradeRecordEntity3.setStockName("交通银行");
		tradeRecordEntity3.setStockCode("006000");
		tradeRecordEntity3.setTradeAmount("20万");
		tradeRecordEntity3.setTradeDate("2013-11-06");
		tradeRecordEntity3.setTradeProfit("+9000.00");
		tradeRecordEntity3.setTradeType("join");
		
		tradeRecordEntities.add(tradeRecordEntity3);
		
		
		entity.setRecords(tradeRecordEntities);
		List<TradeRecordEntity> entities = entity.getRecords();
		join_entities = new ArrayList<TradeRecordEntity>();
		challenge_entities = new ArrayList<TradeRecordEntity>();
		for(TradeRecordEntity entity : entities) {
			if (entity.getTradeType().equals("challenge")) {
				challenge_entities.add(entity);
				challenge_entitiesField.setValue(challenge_entities);
			} else if(entity.getTradeType().equals("join")){
				join_entities.add(entity);
				join_entitiesField.setValue(join_entities);
			}
		}
		
		
		
	}
	
	
	
	@OnShow
	protected void showView() {
		setUserIcon(entity.getuIcon());
		setUserNickName(entity.getuNickName());
		setTotalProfit(entity.getuProfits());
		setTotoalIntegral(entity.getuIntegrals());
		setChallengeSharedNum(entity.getuChallengeShared());
		setJoinSharedNum(entity.getuJoinShared());
	}
	

	/*@OnDataChanged
	protected void dataUpdate(ValueChangedEvent event) {
		if (event.getComponent() == this.user_iconField) {
			this.user_iconField.setAttribute(AttributeKeys.text, entity.getuIcon());
		} else if(event.getComponent() == this.user_nick_nameField) {
			this.user_nick_nameField.setAttribute(AttributeKeys.text, entity.getuNickName());
		}
	}*/

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
	
	protected void setTotoalIntegral(String totalIntegral) {
		this.total_real_integral = totalIntegral;
		this.total_real_integralField.setValue(totalIntegral);
	}
	
	protected void setTotalProfit(String totalProfit) {
		this.total_profit = totalProfit;
		this.total_profitField.setValue(totalProfit);
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
