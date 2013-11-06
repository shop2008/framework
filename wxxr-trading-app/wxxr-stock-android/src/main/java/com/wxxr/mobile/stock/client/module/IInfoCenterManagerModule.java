/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import java.util.List;

import com.wxxr.mobile.stock.client.bean.Stock;

/**
 * @author wangxuyang
 *
 */
public interface IInfoCenterManagerModule {
	/**
	 * 搜索股票
	 * @param keyword-关键词：股票代码或拼音代码
	 * @return
	 */
	List<Stock> searchStock(String keyword);

}
