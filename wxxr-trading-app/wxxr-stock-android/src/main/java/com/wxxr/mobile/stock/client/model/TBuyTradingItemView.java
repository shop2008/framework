package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;

@View(name = "TBuyTradingItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.sell_trading_stock_order_item")
public abstract class TBuyTradingItemView extends ViewBase implements
		IModelUpdater {
	
	StockTradingOrderBean orderBean;
	
	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.stockName:'--'}")
	String stockName;
	
	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.currentPirce:'--'}", attributes={
			@Attribute(name = "textColor", value = "${orderBean.status==0?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String currentPirce;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.buy:'--'}", attributes={
			@Attribute(name = "textColor", value = "${orderBean.status==0?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String buy;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.gain:'--'}", attributes={
			@Attribute(name = "textColor", value = "${orderBean.status==0?'resourceId:color/gray':'resourceId:color/red'}")
			})
	String gain;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.stockCode:'--'}", attributes={
			@Attribute(name = "textColor", value = "${orderBean.status==0?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String stockCode;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.changeRate:'--'}", attributes={
			@Attribute(name = "textColor", value = "${orderBean.status==0?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String changeRate;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.amount:'--'}", attributes={
			@Attribute(name = "textColor", value = "${orderBean.status==0?'resourceId:color/gray':'resourceId:color/red'}")
			})
	String amount;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.gainRate:'--'}", attributes={
			@Attribute(name = "textColor", value = "${orderBean.status==0?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String gainRate;
	@Override
	public void updateModel(Object value) {
		if (value instanceof StockTradingOrderBean) {
			registerBean("orderBean",value);
		}
	}
}
