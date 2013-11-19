/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import java.util.Map;

import com.wxxr.mobile.stock.app.bean.LineListBean;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisListBean;

/**
 * @author wangxuyang
 * 
 */
public interface IInfoCenterManagementService {
	/**
	 * 搜索股票
	 * 
	 * @param keyword
	 *            -关键词：股票代码或拼音代码
	 * @return
	 */
	SearchStockListBean searchStock(String keyword);

	/**
	 * 分时线数据
	 * 
	 * @param code
	 *            - 股票代码
	 * @param market
	 *            - 市场代码
	 * @param start
	 *            - 起始值
	 * @param limit
	 *            - 偏移量
	 * @param date
	 *            - 日期
	 * @param startTime
	 *            - 开始时间戳 如：1329918250
	 * @param endTime
	 *            - 结束时间戳
	 * @param page
	 *            -第几页
	 * @return
	 */
	StockMinuteKBean getMinuteline(Map<String/* code */, String/* market */> params);

	/**
	 * 日K数据
	 * 
	 * @param code
	 * @param market
	 * @return
	 */
	LineListBean getDayline(String code, String market);

	/**
	 * 涨跌排行接口
	 * @param taxis
	 * @param orderby
	 * @param start
	 * @param limit
	 * @param blockId
	 * @return
	 */
	public StockTaxisListBean getStocktaxis(String taxis, String orderby,long start, long limit, long blockId);

}
