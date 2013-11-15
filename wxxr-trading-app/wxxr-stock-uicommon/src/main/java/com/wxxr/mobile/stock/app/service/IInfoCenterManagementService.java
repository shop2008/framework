/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.stock.app.bean.SearchStockListBean;

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
	SearchStockListBean searchStock(String keyword);
	

}
