package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;



@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="TradingAccInfoBean")
public class TradingAccInfo{
	private String maxStockName;//最大持股名称
	private String maxStockCode;//最大持股代码
	private String maxStockMarket;//最大持股市场
	private boolean virtual;//是否为虚拟盘
	private Long sum;//申请资金
	private Long totalGain;//总盈亏
	private int status;//1:表示今天,0:表示前一天
	private String over; //CLOSED表示已经完结,"UNCLOSE"表示未完结
	private long createDate;// 交易盘创建时间
	private Long acctID;//交易盘ID
}
