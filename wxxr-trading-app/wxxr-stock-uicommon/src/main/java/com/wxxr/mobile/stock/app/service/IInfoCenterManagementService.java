/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.stock.app.bean.LineListBean;
import com.wxxr.mobile.stock.app.bean.QuotationListBean;
import com.wxxr.mobile.stock.app.bean.SearchStockListBean;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTaxisBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;

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
	public BindableListWrapper<StockLineBean> getDayStockline(final String code, final String market);
	/**
	 * 涨跌排序接口,默认按涨跌幅降序
	 * @param orderby 排序字段名称，即按什么排序 ：“newprice”-按最新价；“risefallrate”-按涨跌幅
	 * @param direction - 排序方向：升序or降序：“asc”-升序，"desc"-降序
	 * @param start - 起始条目
	 * @param limit - 最多可取条数
	 * @return
	 */
	public BindableListWrapper<StockTaxisBean> getStocktaxis(String orderby, String direction,long start, long limit);
	
	
	public void reloadStocktaxis(String orderby, String direction,long start, long limit);
	
	/**
	 * 获取指数行情数据
	 */
	public QuotationListBean getQuotations();
	/**
	 * 
	 * 获取个股行情数据
	 * @param code
	 * @param market
	 * @return
	 */
	public StockQuotationBean getStockQuotation(String code,String market);
	/**
	 * 
	 * 获取五日分时数据
	 * @param code
	 * @param market
	 * @return
	 */
	public BindableListWrapper<List<StockMinuteKBean>> getFiveDayMinuteline(String code,String market);
}
