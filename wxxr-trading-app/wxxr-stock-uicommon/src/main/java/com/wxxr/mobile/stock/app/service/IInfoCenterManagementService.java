/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.stock.app.bean.LineListBean;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.stock.restful.json.ParamVO;

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
	 * 
	 * @param code - 股票代码
	 * @param market - 市场代码
	 * @param start - 起始值
	 * @param limit - 偏移量
	 * @param date - 日期
	 * @param startTime - 开始时间戳 如：1329918250
	 * @param endTime - 结束时间戳
	 * @param page -第几页
	 * @return
	 */
	StockMinuteKBean getMinuteline(String code, String market);
	LineListBean getDayline(ParamVO paramVO);

}
