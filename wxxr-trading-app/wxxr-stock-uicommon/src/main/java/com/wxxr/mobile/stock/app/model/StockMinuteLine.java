package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * 分时线
 * 
 * @author wangxuyang
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className = "StockMinuteLineBean")
public class StockMinuteLine{
	private Long price;// 价格(最新价)
	private Long secuvolume;// 成交量
	private Long secuamount;// 成交额
	private Long avprice;// 均线价格
	private String hqTime;//补充时间，为了测试
	private Long avgChangeRate;//上证指数，和深圳成指数的平均涨跌幅
	
	
}
