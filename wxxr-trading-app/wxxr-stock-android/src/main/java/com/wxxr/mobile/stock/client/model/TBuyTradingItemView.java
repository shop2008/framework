package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name = "TBuyTradingItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.buy_trading_stock_order_item")
public abstract class TBuyTradingItemView extends ViewBase implements
		IModelUpdater {
	
	StockTradingOrderBean orderBean;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorYuan;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorNoSign;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.stockName:'--'}")
	String stockName;
	
	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.currentPirce:'--'}", converter = "stockLong2StringConvertorNoSign", attributes={
			@Attribute(name = "textColor", value = "${orderBean==null?'resourceId:color/gray':orderBean.changeRate>0?'resourceId:color/red':(orderBean.changeRate<0?'resourceId:color/green':'resourceId:color/gray')}")
			})
	String currentPirce;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.buy:'--'}", converter = "stockLong2StringConvertorYuan", attributes={
			@Attribute(name = "textColor", value = "${orderBean.status==0?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String buy;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.gain:'--'}", converter = "stockLong2StringConvertorYuan", attributes={
			@Attribute(name = "textColor", value = "${orderBean==null?'resourceId:color/gray':orderBean.gain>0?'resourceId:color/red':(orderBean.gain<0?'resourceId:color/green':'resourceId:color/gray')}")
			})
	String gain;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.stockCode:'--'}", attributes={
			@Attribute(name = "textColor", value = "${orderBean.status==0?'resourceId:color/gray':'resourceId:color/gray'}")
			})
	String stockCode;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.changeRate:'--'}", converter = "stockLong2StringConvertorSpecial", attributes={
			@Attribute(name = "textColor", value = "${orderBean==null?'resourceId:color/gray':orderBean.changeRate>0?'resourceId:color/red':(orderBean.changeRate<0?'resourceId:color/green':'resourceId:color/gray')}")
			})
	String changeRate;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.amount:'--'}${'股'}", attributes={
			@Attribute(name = "textColor", value = "${orderBean.status==0?'resourceId:color/gray':'resourceId:color/gray'}")
			})
	String amount;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.gainRate:'--'}", converter = "stockLong2StringConvertorSpecial", attributes={
			@Attribute(name = "textColor", value = "${orderBean==null?'resourceId:color/gray':orderBean.gainRate>0?'resourceId:color/red':(orderBean.gainRate<0?'resourceId:color/green':'resourceId:color/gray')}")
			})
	String gainRate;
	@Override
	public void updateModel(Object value) {
		if (value instanceof StockTradingOrderBean) {
			registerBean("orderBean",value);
		}
	}
}
