/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;


/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="StockTradingOrderBean")
public class StockTradingOrder{
	/**
	 * 订单ID
	 */
	private Long id;
	/**
	 * 股票代码
	 */
	private String stockCode;
	/**
	 * 股票名称
	 */
	private String stockName;
	/**
	 * 市场代码
	 */
	private String marketCode;
	/**
	 * 当前价
	 */
	private Long currentPirce;
	/**
	 * 当前涨幅
	 */
	private Long changeRate;
	/**
	 * 委托价
	 */
	private Long buy;
	/**
	 * 委托数量
	 */
	private Long amount;
	/**
	 * 当前收益
	 */
	private Long gain;
	/**
	 * 当前收益率
	 */
	private Long gainRate;
	/**
	 * 订单状态
	 */
	private String status;
	
	
}
