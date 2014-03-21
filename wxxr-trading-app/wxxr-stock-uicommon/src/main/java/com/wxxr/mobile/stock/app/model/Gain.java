package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;

@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="GainBean")
public class Gain{
	/**最大持股代码*/
	private String maxStockCode;
	/**最大持股名称*/
	private String maxStockName;
	/**最大持股市场*/
	private String maxStockMarket;
	/**现金收益*/
	private Long userGain;
	/**结算时间*/
	private String closeTime;
	/**交易盘类型*/
	private boolean virtual; 
	/**交易盘唯一标识*/
	private Long tradingAccountId;
	/**交易盘额度*/
	private Long sum;
	/**交易盘操盘利润,我的交易记录*/
	private Long totalGain;
	/**1:T日交易盘，0：T+1交易盘*/
	private int status;
	/**交易盘状态（完结/未完结）*/
	private String over;
	
	
	
}
