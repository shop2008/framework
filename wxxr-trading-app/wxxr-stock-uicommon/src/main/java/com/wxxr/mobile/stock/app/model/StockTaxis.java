package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * 涨跌排行VO
 * 
 * @author wangxuyang
 * 
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className = "StockTaxisBean")
public class StockTaxis{
	private String name;// 无限新锐
	private String code;
	private String market;
	private Long newprice;// 最新
	private Long risefallrate;// 涨跌幅
	private Long secuvolume;// 成交量
	private Long secuamount;// 成交额
	private Long profitrate;// 市盈率
	private Long handrate;// 换手率
	private Long marketvalue;// 市值
	private Long lb;// 量比
}
