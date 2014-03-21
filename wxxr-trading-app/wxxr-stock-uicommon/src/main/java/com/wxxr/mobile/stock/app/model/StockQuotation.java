package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;


/**
 * 行情数据-五档盘口VO
 * 
 * @author wangxuyang
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="StockQuotationBean")
public class StockQuotation {
	// datetime 2011-12-27/13:26:56"
	private String code;// 股票或指数 代码
	private String market;// 市场代码： SH，SZ各代表上海，深圳。
	private String datetime;
	private Long close;// 昨收
	private Long open;// 开盘
	private Long high;// 最高
	private Long low;// 最低
	private Long newprice;// 最新
	private Long averageprice;// 均价
	private Long secuvolume;// 成交量
	private Long secuamount;// 成交额
	private Long lb;// 量比
	private Long profitrate;// 市盈率
	private Long handrate;// 换手率
	private Long risefallrate;// 涨跌幅
	private Long marketvalue;// 市值
	private Long capital;// 流通盘
	private Long buyprice1;// 买1
	private Long buyprice2;// 买2
	private Long buyprice3;// 买3
	private Long buyprice4;// 买4
	private Long buyprice5;// 买5
	private Long buyvolume1;// 买1量
	private Long buyvolume2;// 买2量
	private Long buyvolume3;// 买3量
	private Long buyvolume4;// 买4量
	private Long buyvolume5;// 买5量
	private Long sellprice1;// 卖1
	private Long sellprice2;// 卖2
	private Long sellprice3;// 卖3
	private Long sellprice4;// 卖4
	private Long sellprice5;// 卖5
	private Long sellvolume1;// 卖1量
	private Long sellvolume2;// 卖2量
	private Long sellvolume3;// 卖3量
	private Long sellvolume4;// 卖4量
	private Long sellvolume5;// 卖5量



	// 以下为指数属性
	private Long ppjs;// 平盘家数
	private Long szjs;// 上涨家数
	private Long xdjs;// 下跌家数

	// 股票买盘，卖盘
	private Long sellsum;// 买盘
	private Long buysum;// 卖盘
	// 涨跌额
	private Long change;
	// status 1：正常 ：2 ：停牌
	private Long status;
	
}
