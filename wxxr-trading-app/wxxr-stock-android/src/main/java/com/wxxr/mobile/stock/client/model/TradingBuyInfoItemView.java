package com.wxxr.mobile.stock.client.model;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

@View(name="TradingBuyInfoItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.trading_buy_info_item")
public abstract class TradingBuyInfoItemView extends ViewBase implements IModelUpdater{
	final Trace log = Trace.register(TradingBuyInfoItemView.class);
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="multiple",value="100"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Bean
	TradingAccInfoBean trading;
	
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;
		
	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(trading!=null?trading.maxStockCode:'', trading!=null?trading.maxStockMarket:'')}")
	StockBaseInfo stockInfoBean;
	
	/**股票名称*/
	@Field(valueKey="text",binding="${stockInfoBean!=null&&stockInfoBean.name!=null?stockInfoBean.name:'无持仓'}",attributes={
			@Attribute(name = "textColor", value = "${(trading.over!=null&&trading.over=='CLOSED')?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String stockName;
	
	/**股票代码*/
	@Field(valueKey="text",binding="${trading!=null&&trading.maxStockCode!=null?trading.maxStockCode:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(trading.over!=null&&trading.over=='CLOSED')?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String stockCode;
	
	/**额度（申请资金）*/
	@Field(valueKey="text",binding="${trading!=null?trading.sum:null}",attributes={
			@Attribute(name = "textColor", value = "${(trading.over!=null&&trading.over=='CLOSED')?'resourceId:color/gray':'resourceId:color/white'}")
			},converter="stockLong2StringAutoUnitConvertor")
	String initCredit;
	
	/**总收益*/
	@Field(valueKey="text",binding="${trading!=null?trading.totalGain:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(trading.over!=null&&trading.over=='CLOSED') ? 'resourceId:color/gray': (trading.totalGain > 0 ? 'resourceId:color/red' : (trading.totalGain < 0 ? 'resourceId:color/green':'resourceId:color/white'))}")
			},converter="stockLong2StringAutoUnitConvertor")	
	String income;
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	@Field(valueKey="enabled",binding="${trading!=null && !trading.virtual}")
	boolean type;
	@Override
	public void updateModel(Object data) {
		if(data instanceof TradingAccInfoBean){
			this.trading = (TradingAccInfoBean) data;
			registerBean("trading", this.trading);
		}
	}
}
