/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.annotation.BindableBean;


/**
 * 交易盘
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="TradingAccountBean")
public class TradingAccount {
	private Long id;
	private int type;//交易盘类型  0-模拟盘；1-实盘
	private String stockCode;//最大持股代码
	private String stockName;//最大持股名称
	private String createDate;//申请日期
	private String endDate;//截止日期
	private float initCredit;//额度（申请资金）
	private float income;//总收益
	private int status;//状态 0-未结算 ； 1-已结算
	private float available;//可用资金
	
}
