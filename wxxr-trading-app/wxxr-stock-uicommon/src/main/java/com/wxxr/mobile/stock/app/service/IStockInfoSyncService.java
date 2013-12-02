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
	
	List<StockBaseInfo> getStockInfos(IEntityFilter<StockBaseInfo> filter);
	StockBaseInfo getStockBaseInfoByCode(String code);
}
