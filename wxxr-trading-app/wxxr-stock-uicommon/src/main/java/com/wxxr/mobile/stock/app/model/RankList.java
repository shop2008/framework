/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;


/**
 * 排行榜
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="RankListBean")
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
	/**
	 * 赚钱榜
	 */
	private List<EarnRankItemBean> earnRankBeans;

}
