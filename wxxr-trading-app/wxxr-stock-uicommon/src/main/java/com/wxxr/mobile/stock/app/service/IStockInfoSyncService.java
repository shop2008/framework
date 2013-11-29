/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import java.util.List;

import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.sync.model.StockBaseInfo;

/**
 * @author wangxuyang
 *
 */
public interface IStockInfoSyncService {
	
	List<StockBaseInfo> getStockInfos(IEntityFilter<StockBaseInfo> filter);
	StockBaseInfo getStockBaseInfoByCode(String code);
}
