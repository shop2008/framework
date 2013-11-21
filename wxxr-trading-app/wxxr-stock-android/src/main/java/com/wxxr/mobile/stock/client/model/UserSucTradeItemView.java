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
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;

@View(name="tradeRecordMoreItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_trade_record_item_layout")
public abstract class UserSucTradeItemView extends ViewBase implements IModelUpdater{

	GainBean accountBean;
	
	/**股票名称*/
	@Field(valueKey="text",binding="${accountBean!=null?accountBean.maxStockName:'--'}",attributes={
			@Attribute(name = "textColor", value = "${accountBean.status==1?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String stockName;
	
	/**股票代码*/
	@Field(valueKey="text",binding="${accountBean!=null?accountBean.maxStockCode:'--'}",attributes={
			@Attribute(name = "textColor", value = "${accountBean.status==1?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String stockCode;
	
	/**额度（申请资金）*/
	@Field(valueKey="text",binding="${accountBean!=null?accountBean.sum:'--'}",attributes={
			@Attribute(name = "textColor", value = "${accountBean.status==1?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String initCredit;
	
	/**总收益*/
	@Field(valueKey="text",binding="${accountBean!=null?accountBean.userGain:'--'}",attributes={
			@Attribute(name = "textColor", value = "${accountBean.status == 1 ? 'resourceId:color/gray': (accountBean.userGain > 0 ? 'resourceId:color/red' : (accountBean.userGain < 0 ? 'resourceId:color/green':'resourceId:color/white'))}")
			},
			converter="stockLong2StringAutoUnitConvertor"
			)	
	String income;
	
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.closeTime:'--'}", converter="longTime2StringConvertor")
	String date;
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	@Field(valueKey="enabled", binding="${accountBean!=null && accountBean.virtual==true?true:false}")
	boolean type;
	
	@Convertor(params={
			@Parameter(name="format",value="MM-dd")
	})
	LongTime2StringConvertor longTime2StringConvertor;
	
	@Convertor(params={
			@Parameter(name="format", value="%10.2f")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof GainBean) {
			registerBean("accountBean", value);
		}
	}

}
