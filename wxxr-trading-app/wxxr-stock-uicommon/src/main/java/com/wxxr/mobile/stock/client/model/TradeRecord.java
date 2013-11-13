package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.annotation.BindableBean;

@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="TradeRecord")
public class TradeRecord {

	/**
	 * 股票名称
	 */
	private String stockName;
	
	/**
	 * 股票代码
	 */
	private String stockCode;
	
	/**
	 * 额度
	 */
	private String tradeAmount;
	
	/**
	 * 盈亏
	 */
	private String tradeProfit;
	
	
	/**
	 * 交易日期
	 */
	private String tradeDate;
	
	/**
	 * 交易类型
	 */
	private String tradeType;

	
}
