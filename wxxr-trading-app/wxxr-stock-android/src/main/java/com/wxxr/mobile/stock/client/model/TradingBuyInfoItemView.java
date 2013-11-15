package com.wxxr.mobile.stock.client.model;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;

@View(name="TradingBuyInfoItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.trading_buy_info_item")
public abstract class TradingBuyInfoItemView extends ViewBase implements IModelUpdater {
	final Trace log = Trace.register(TradingMainView.class);
	
	TradingAccountBean trading;
	
	/**股票名称*/
	@Field(valueKey="text",binding="${trading!=null?trading.stockName:'--'}",attributes={
			@Attribute(name = "textColor", value = "${trading.status==1?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String stockName;
	
	/**股票代码*/
	@Field(valueKey="text",binding="${trading!=null?trading.stockCode:'--'}",attributes={
			@Attribute(name = "textColor", value = "${trading.status==1?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String stockCode;
	
	/**额度（申请资金）*/
	@Field(valueKey="text",binding="${trading!=null?trading.initCredit:'--'}",attributes={
			@Attribute(name = "textColor", value = "${trading.status==1?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String initCredit;
	
	/**总收益*/
	@Field(valueKey="text",binding="${trading!=null?trading.income:'--'}",attributes={
			@Attribute(name = "textColor", value = "${trading.status == 1 ? 'resourceId:color/gray': (trading.income > 0 ? 'resourceId:color/red' : (trading.income < 0 ? 'resourceId:color/green':'resourceId:color/white'))}")
			})	
	String income;
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	@Field(valueKey="enabled",binding="${trading!=null && trading.type==0?false:true}")
	boolean type;
	
	@Override
	public void updateModel(Object data) {
		if(data instanceof TradingAccountBean){
			registerBean("trading", data);
		}
	}
}
