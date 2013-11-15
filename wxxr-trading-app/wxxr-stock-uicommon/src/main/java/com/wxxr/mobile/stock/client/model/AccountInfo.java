package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.client.bean.ScoreBean;
import com.wxxr.mobile.stock.client.bean.TradeDetailBean;

@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="AccountInfoBean")
public class AccountInfo {

	/**
	 * 余额
	 */
	private String balance;
	
	/**
	 * 冻结
	 */
	private String freeze;
	
	/**
	 * 可用
	 */
	private String avalible;
	
	/**
	 * 积分余额
	 */
	private String scoreBalance;
	
	
	/**
	 * 积分信息
	 */
	private List<ScoreBean> scores;
	
	/**
	 * 收支明细
	 */
	private List<TradeDetailBean> tradeDetails;

}
