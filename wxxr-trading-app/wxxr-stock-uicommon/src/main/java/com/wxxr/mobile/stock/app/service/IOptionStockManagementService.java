package com.wxxr.mobile.stock.app.service;

import java.util.Map;

import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;

public interface IOptionStockManagementService {
	void add(String stockCode,String mc);
	void delete(String stockCode,String mc);	
	BindableListWrapper<StockQuotationBean> getMyOptionStocks(String taxis, String orderby);
	void updateOrder(Map<String,Integer> orders);
}
