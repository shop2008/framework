package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

@View(name="sellTradingStockOrder")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.sell_trading_stock_order_item")
public abstract class SellTradingStockOrderItemView extends ViewBase implements IModelUpdater {

	@Bean
	StockTradingOrderBean stockTradingOrder; 
	
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;
		
	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(stockTradingOrder!=null?stockTradingOrder.stockCode:'', stockTradingOrder!=null?stockTradingOrder.marketCode:'')}")
	StockBaseInfo stockInfoBean;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="100.00"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="1000.00"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial1;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorYuan;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="100.00"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorNoSign;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	/**订单ID*/
	long id;
	
	/**股票代码*/
	@Field(valueKey="text",binding="${stockTradingOrder!=null?stockTradingOrder.stockCode:'--'}")
	String stockCode;
	
	/**股票名称*/
	@Field(valueKey="text",binding="${stockInfoBean!=null?stockInfoBean.name:'--'}")
	String stockName;
	
	/**当前价*/
	@Field(valueKey="text",binding="${stockTradingOrder!=null?stockTradingOrder.currentPirce:null}",converter = "stockLong2StringConvertorNoSign",attributes={
			@Attribute(name = "textColor", value = "${stockTradingOrder==null?'resourceId:color/gray':stockTradingOrder.changeRate>0?'resourceId:color/stock_text_up':(stockTradingOrder.changeRate<0?'resourceId:color/stock_text_down':'resourceId:color/gray')}")
	})
	String currentPirce;
	
	/** 当前涨幅*/
	@Field(valueKey="text",binding="${stockTradingOrder!=null?stockTradingOrder.changeRate:null}",converter = "stockLong2StringConvertorSpecial1",attributes={
			@Attribute(name = "textColor", value = "${stockTradingOrder==null?'resourceId:color/gray':stockTradingOrder.changeRate>0?'resourceId:color/stock_text_up':(stockTradingOrder.changeRate<0?'resourceId:color/stock_text_down':'resourceId:color/gray')}")
	})
	String changeRate;
	
	/** 委托价*/
	@Field(valueKey="text",binding="${stockTradingOrder!=null?stockTradingOrder.buy:null}",converter="stockLong2StringConvertorYuan")
	String buy;
	
	/**委托数量*/
	@Field(valueKey="text",binding="${stockTradingOrder!=null?stockTradingOrder.amount:'--'}${'股'}")
	String amount;
	
	/**当前收益*/
	@Field(valueKey="text",binding="${stockTradingOrder!=null?stockTradingOrder.gain:null}",converter = "stockLong2StringConvertorYuan",attributes={
			@Attribute(name = "textColor", value = "${stockTradingOrder==null?'resourceId:color/gray':stockTradingOrder.gain>0?'resourceId:color/stock_text_up':(stockTradingOrder.gain<0?'resourceId:color/stock_text_down':'resourceId:color/gray')}")
	})
	String gain;
	
	/**当前收益率*/
	@Field(valueKey="text",binding="${stockTradingOrder!=null?stockTradingOrder.gainRate:null}",converter = "stockLong2StringConvertorSpecial",attributes={
			@Attribute(name = "textColor", value = "${stockTradingOrder==null?'resourceId:color/gray':stockTradingOrder.gainRate>0?'resourceId:color/stock_text_up':(stockTradingOrder.gainRate<0?'resourceId:color/stock_text_down':'resourceId:color/gray')}")
	})
	String gainRate;
	
	/**订单状态*/
	String status;
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof StockTradingOrderBean){
			registerBean("stockTradingOrder", value);
		}
	}
}
