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
@BindableBean
public class DealDetail{
	
	/**申请资金*/
	private String fund;
	/**总盈亏率*/
	private float plRisk;
	/**盈亏总额（交易盘，除去费用）*/
	private float totalGain;
	/**玩家实得收益--没有收益时不显示80%*/
	private float userGain;
	private String[] imgUrl;
	private List<TradingRecord> tradingRecords;

	
	
}
