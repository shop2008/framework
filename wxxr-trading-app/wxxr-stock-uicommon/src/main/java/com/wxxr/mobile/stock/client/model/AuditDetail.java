package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * 
 * @author chenchao
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="AuditDetailBean")
public class AuditDetail{
	/**申请资金*/
	private String fund;
	/**总盈亏率*/
	private String plRisk;
	/**盈亏总额（交易盘，除去费用）*/
	private String totalGain;
	/**玩家实得收益--没有收益时不显示80%*/
	private String userGain;
	/**补偿交易综合费--总收益<手续费，=总收益（优先补偿)*/
	private String tradingCost;
	/**账户管理费--没有收益时不显示20%*/
	private String accountPay;
	/**止损比例*/
	private String capitalRate;
	/**冻结资金*/
	private String frozenAmount;
	/**扣减数*/
	private String payOut;
	/**解冻数量*/
	private String unfreezeAmount;
	/**买入均价*/
	private String buyAverage;
	/**卖出均价*/
	private String sellAverage;
	/**交易结算日期*/
	private String tradingDate;
	/**交易盘类型*/
	private boolean virtual;
	/**申请交易盘时间*/
	private String buyDay;
	/**存续时间*/
	private String deadline;
	/**手续费*/
	private String cost;
	/**交易盘编号*/
	private String id;
	/**交易盘类型*/
	private String type;
	
	
	
}
