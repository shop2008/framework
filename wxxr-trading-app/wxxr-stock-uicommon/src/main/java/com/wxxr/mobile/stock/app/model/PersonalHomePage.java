package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="PersonalHomePageBean")
public class PersonalHomePage{
	/**
	 * 实盘积分
	 */
	private Long voucherVol;
	/**
	 * 收益率
	 */
	private double totalProfit;
	/**
	 * 挑战交易盘分享笔数
	 */
	private long actualCount;
	/**
	 * 参赛交易盘分享笔数
	 */
	private long virtualCount;
	/**
	 * 挑战交易盘列表
	 */
	private List<GainBean> actualList;
	/**
	 * 参赛交易盘列表
	 */
	private List<GainBean> virtualList;
	
	
	
	
	
}
