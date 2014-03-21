package com.wxxr.mobile.stock.app.service;

import java.util.Map;

import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.db.OptionStock;

public interface IOptionStockManagementService {
	void add(String stockCode,String mc);
	void delete(String stockCode,String mc);	
	OptionStock find(String stockCode,String mc);
	void update(OptionStock optionStock);
	BindableListWrapper<StockQuotationBean> getMyOptionStocks(String taxis, String orderby);
	void updateOrder(Map<String,Integer> orders);
	boolean isAdded(String stockCode,String mc);
	/**
	 * 获取自选股行情
	 * @param stockCode
	 * @param mc
	 * @return
	 */
	StockQuotationBean getOptionStockQuotation(String stockCode,String mc);
}
