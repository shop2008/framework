package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.annotation.BindableBean;

@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="TradingRecordBean")
public class TradingRecord {
	private long date;// 日期
	private String market;// 股票市场
	private String code;// 股票代码
	private String describe;// 成交方向：
	private long price;// 成交价格
	private long vol;// 成交量
	private long amount;// 成交金额
	private long brokerage;// 佣金
	private long tax;// 印花税
	private long fee;// 过户费
	private int day;// 1:表示t日,0:表示t+1日
	private boolean beDone;// 订单是否完成，DONE为true；否则为false
}