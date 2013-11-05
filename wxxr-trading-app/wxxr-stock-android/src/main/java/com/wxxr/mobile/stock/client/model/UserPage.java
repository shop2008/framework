package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.bean.Article;
import com.wxxr.mobile.stock.client.bean.TradeRecordEntity;
import com.wxxr.mobile.stock.client.bean.UserInfoEntity;

/**
 * 个人主页
 * @author renwenjie
 */
@View(name="user_page")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_page_layout")
public abstract class UserPage extends PageBase implements IModelUpdater {

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
	
	/*@Field(valueKey="options")
	List<TradeRecordEntity> challengeTrades;
	
	@Field(valueKey="options")
	List<TradeRecordEntity> joinTrades;*/
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof UserInfoEntity) {
			UserInfoEntity entity = (UserInfoEntity) value;
			user_icon = entity.getuIcon();
			user_nick_name = entity.getuNickName();
			total_real_integral = entity.getuIntegrals();
			total_profit = entity.getuProfits();
			challenge_shared_num_what = entity.getuChallengeShared();
			join_shared_num_what = entity.getuJoinShared();
		}
	}
	

	@Override
	protected void onShow(IBinding<IView> binding) {
		super.onShow(binding);
		/*初始化数据*/
//		entity = new UserInfoEntity();
//		entity.setuIcon("resourceId:drawable/home");
//		entity.setuNickName("王三");
//		entity.setuIntegrals("150000");
//		entity.setuProfits("10000");
//		entity.setuChallengeShared("23");
//		entity.setuJoinShared("20");
		
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
}
