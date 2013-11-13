/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="StockBasicMarketInfoBean")
public class StockBasicMarketInfo {
	private String code;
	private String name;
	private String pyCode;//拼音代码	
	private String currentTime;
	private float lastDayPrice;//昨收价
	private float todayInitPrice;//今开价
	private float currentPrice;
	private float highestPrice;
	private float lowestPrice;
}
