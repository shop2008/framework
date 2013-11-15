package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * 收支详情
 * @author renwenjie
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="TradeDetailBean")
public class TradeDetail {
	
	/**
	 * 交易类别
	 */
	private String tradeCatagory;
	
	/**
	 * 交易日期
	 */
	private String tradeDate;
	
	/**
	 * 交易额度
	 */
	private String tradeAmount;

	
}
