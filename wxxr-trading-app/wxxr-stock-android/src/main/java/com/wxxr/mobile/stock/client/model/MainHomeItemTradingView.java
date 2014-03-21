package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.v2.bean.TradingAccountMenuItem;
import com.wxxr.mobile.stock.client.utils.Float2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name = "MainHomeItemTradingView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.main_home_item_trading_view")
public abstract class MainHomeItemTradingView extends ViewBase implements
		IModelUpdater {

	@Bean
	TradingAccountMenuItem tradingAccountBean;

	@Convertor(params = { 
			@Parameter(name = "format", value = "%+.2f%%")})
	Float2StringConvertor stockLong2StringConvertorSpecial;

	@Convertor(params = { @Parameter(name = "format", value = "%+.2f元"),
			@Parameter(name = "multiple", value = "100.00") })
	StockLong2StringConvertor stockLong2StringConvertor;

	@Field(valueKey = "imageURI", binding = "${'resourceId:drawable/trading'}${(tradingAccountBean!=null&&tradingAccountBean.type!=null)?tradingAccountBean.type:''}")
	String icon;

	@Field(valueKey = "imageURI", binding = "${'resourceId:drawable/status'}${(tradingAccountBean!=null&&tradingAccountBean.status!=null)?tradingAccountBean.status:''}")
	String status;

	@Field(valueKey = "text", binding = "${tradingAccountBean!=null?tradingAccountBean.title:'--'}")
	String title;

	@Field(valueKey = "text", binding = "${tradingAccountBean!=null?tradingAccountBean.date:'--'}")
	String date;

	@Field(valueKey = "text", binding = "${tradingAccountBean!=null?(tradingAccountBean.maxHoldStockName==null||tradingAccountBean.maxHoldStockName==''?'无持仓':tradingAccountBean.maxHoldStockName):'--'}", attributes = { @Attribute(name = "textColor", value = "${tradingAccountBean==null||tradingAccountBean.status == '2'?'resourceId:color/gray':tradingAccountBean.incomeRate>0?'resourceId:color/stock_text_up':(tradingAccountBean.incomeRate<0?'resourceId:color/stock_text_down':'resourceId:color/gray')}") })
	String name;

	@Field(valueKey = "text", binding = "${tradingAccountBean!=null?tradingAccountBean.incomeRate:'0'}", converter = "stockLong2StringConvertorSpecial", attributes = { @Attribute(name = "textColor", value = "${tradingAccountBean==null||tradingAccountBean.status == '2'?'resourceId:color/gray':tradingAccountBean.incomeRate>0?'resourceId:color/stock_text_up':(tradingAccountBean.incomeRate<0?'resourceId:color/stock_text_down':'resourceId:color/gray')}") })
	String incomeRate;

	@Field(valueKey = "text", binding = "${tradingAccountBean!=null?tradingAccountBean.income:'0'}", converter = "stockLong2StringConvertor", attributes = { @Attribute(name = "textColor", value = "${tradingAccountBean==null||tradingAccountBean.status == '2'?'resourceId:color/gray':tradingAccountBean.income>0?'resourceId:color/stock_text_up':(tradingAccountBean.income<0?'resourceId:color/stock_text_down':'resourceId:color/gray')}") })
	String income;

	@Override
	public void updateModel(Object value) {
		if (value instanceof TradingAccountMenuItem) {
			tradingAccountBean = (TradingAccountMenuItem) value;
			registerBean("tradingAccountBean", value);
		}
	}
}
