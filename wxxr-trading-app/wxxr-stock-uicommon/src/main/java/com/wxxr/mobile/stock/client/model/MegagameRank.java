package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * T、T+1日排行榜
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="MegagameRankBean")
public class MegagameRank{
	/**
	 * 昵称
	 */
	private String nickName;//昵称
	/**
	 * 1:表示今天,0:表示前一天
	 */
	private int status;
	/**
	 * 结算状态-CLOSED表示已经完结,"UNCLOSE"表示未完结
	 */
	private String over; 
	private String maxStockCode;//最大持股代码
	private String maxStockMarket;//最大持股市场
	private Long totalGain;//总盈亏2
	private String gainRate;//总盈亏率1
	private int gainRates;
	private String userId;//用户id
	private long acctID;
	private int rankSeq;
	
}
