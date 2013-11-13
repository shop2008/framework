/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * @author wangyan
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="DealDetailBean")
public class DealDetail{
	
	/**申请资金*/
	private String fund;
	/**总盈亏率*/
	private String plRisk;
	/**盈亏总额（交易盘，除去费用）*/
	private String totalGain;
	/**玩家实得收益--没有收益时不显示80%*/
	private String userGain;
	/**
	 * 收益图
	 */
	private String[] imgUrl;
	/**
	 * 交易记录
	 */
	private List<TradingRecord> tradingRecords;

	
	
}
