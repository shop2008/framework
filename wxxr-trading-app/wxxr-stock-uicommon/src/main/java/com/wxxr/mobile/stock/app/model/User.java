/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;


/**
 * @author wangxuyang
 * 
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="UserBean")
public class User {
	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 用户主页背景
	 */
	private String homeBack;

	/**
	 * 用户形象照
	 */
	private String userPic;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 电话号吗
	 */
	private String phoneNumber;
	
	/**
	 * 未读消息
	 */
	private String unReadMsg;
	/**
	 * 积分
	 */
	private String score;

	/**
	 * 余额
	 */
	private String balance;

	/**
	 * 累计总收益
	 */
	private String totoalProfit;

	/**
	 * 累计实盘积分
	 */
	private String totoalScore;

	/**
	 * 挑战交易盘分享笔数
	 */
	private String challengeShared;

	/**
	 * 参赛交易盘分享笔数
	 */
	private String joinShared;

//	private List<TradingAccountBean> tradeInfos;
}
