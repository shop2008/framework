/**
 * 
 */
package com.wxxr.mobile.stock.client.service;

import java.util.List;

import com.wxxr.mobile.stock.client.bean.StockBean;

/**
 * @author wangxuyang
 *
 */
public interface IInfoCenterManagementService {
	/**
	 * 搜索股票
	 * @param keyword-关键词：股票代码或拼音代码
	 * @return
	 */
	List<StockBean> searchStock(String keyword);
	
	//List<StockBasicMarketInfo> getStockMarketInfos(int start,int limit);

}
