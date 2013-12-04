package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

@View(name = "TBuyTradingItemOrderView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.buy_trading_stock_order_item")
public abstract class TBuyTradingItemOrderView extends ViewBase implements
		IModelUpdater {
	//查股票名称
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;

	//交易服务
	@Bean(type = BindingType.Service)
	ITradingManagementService manageService;
		
	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(orderBean!=null?orderBean.stockCode:'', orderBean!=null?orderBean.marketCode:'')}")
	StockBaseInfo stockInfoBean;
	
	@Bean
	StockTradingOrderBean orderBean;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorNoSign;
	
	@Field(valueKey = "text", binding = "${stockInfoBean!=null?stockInfoBean.name:'--'}")
	String stockName;
	
	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.buy:'--'}", converter = "stockLong2StringConvertorNoSign")
	String buy;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.stockCode:'--'}")
	String stockCode;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.amount:'--'}${'股'}")
	String amount;

	@Field(valueKey = "text")//, binding = "${orderBean!=null?orderBean.status:'--'}${'股'}")
	String status;
	
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof StockTradingOrderBean) {
			registerBean("orderBean",value);
		}
	}
	
	@Command
	String handlerCancelClick(InputEvent event) {
		manageService.cancelOrder(orderBean.getId()+"");
		return null;
	}
}
