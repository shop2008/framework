/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import java.util.List;

import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

/**
 * @author wangxuyang
 *
 */
public interface IStockInfoSyncService {
	/**
	 * 按过滤器过滤股票
	 * @param filter
	 * @return
	 */
	List<StockBaseInfo> getStockInfos(IEntityFilter<StockBaseInfo> filter);
	/**
	 * 根据股票代码和市场代码获取股票
	 * @param code
	 * @param marketCode
	 * @return
	 */
	StockBaseInfo getStockBaseInfoByCode(String code,String marketCode);
}
