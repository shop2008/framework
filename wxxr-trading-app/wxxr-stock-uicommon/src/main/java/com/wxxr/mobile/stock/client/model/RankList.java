/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.client.bean.MegagameRankBean;
import com.wxxr.mobile.stock.client.bean.RegularTicketBean;
import com.wxxr.mobile.stock.client.bean.WeekRankBean;


/**
 * 排行榜
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="RankListBean")
public class RankList {
	/**
	 * 周赛排行榜
	 */
	private List<WeekRankBean> weekRanKBeans;
	/**
	 * T日排行榜
	 */
	private List<MegagameRankBean> tRankBeans;
	/**
	 * T+1排行榜
	 */
	private List<MegagameRankBean> t1RankBeans;
	/**
	 * 实盘券排行榜
	 */
	private List<RegularTicketBean> regularTicketBeans;

}
