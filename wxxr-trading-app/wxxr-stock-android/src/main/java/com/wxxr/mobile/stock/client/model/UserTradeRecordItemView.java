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
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.mobile.stock.client.utils.String2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StringTime2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

@View(name="tradeRecordItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_trade_record_item_layout")
public abstract class UserTradeRecordItemView extends ViewBase implements IModelUpdater {

	GainBean accountBean;
	
	/**股票名称*/
	@Field(valueKey="text",binding="${stockInfoBean!=null?stockInfoBean.name:'无持仓'}",attributes={
			@Attribute(name = "textColor", value = "${accountBean.status==1?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String stockName;
	
	/**股票代码*/
	@Field(valueKey="text",binding="${(accountBean!=null&&accountBean.maxStockCode!=null)?accountBean.maxStockCode:'---'}",attributes={
			@Attribute(name = "textColor", value = "${accountBean.status==1?'resourceId:color/gray':'resourceId:color/white'}"
					)
			})
	String stockCode;
	
	/**额度（申请资金）*/
	@Field(valueKey="text",binding="${accountBean!=null?accountBean.sum:null}",attributes={
			@Attribute(name = "textColor", value = "${accountBean.status==1?'resourceId:color/gray':'resourceId:color/white'}")
			}, converter="applyAmountConvertor")
	String initCredit;
	
	/**总收益*/
	@Field(valueKey="text",binding="${accountBean!=null?accountBean.totalGain:null}",attributes={
			@Attribute(name = "textColor", value = "${accountBean.status==1?'resourceId:color/gray':(accountBean.totalGain > 0 ? 'resourceId:color/red' : (accountBean.totalGain < 0 ? 'resourceId:color/green':'resourceId:color/white'))}")
			},converter="profitConvertor")	
	String income;
	
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.closeTime:'--'}",converter="stringT2StrConvertor",attributes={
			@Attribute(name="textColor", value="${accountBean.status==1?'resourceId:color/gray':'resourceId:color/white'}")})
	String date;
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	@Field(valueKey="enabled", binding="${accountBean.virtual==true?false:true}")
	boolean type;
	
	
	@Convertor(params={@Parameter(name="format", value="%.0f")})
	StockLong2StringAutoUnitConvertor stock2StrConvertor;
	
	@Convertor(
			params={
					@Parameter(name="format", value="%.0f"),
					@Parameter(name="multiple", value="1000000"),
					@Parameter(name="formatUnit",value="万"),
					@Parameter(name="nullString",value="--")
			
				}
			)
	StockLong2StringConvertor applyAmountConvertor;
	
	
	@Convertor(params={
			@Parameter(name="format", value="%.2f"),
			@Parameter(name="multiple",value="100.00f"),
			@Parameter(name="formatUnit",value="元"),
			@Parameter(name="nullString",value="--")
			}
	)
	StockLong2StringConvertor profitConvertor;
	
	@Convertor(params={@Parameter(name="format", value="M月d日")})
	StringTime2StringConvertor stringT2StrConvertor;
	
	
	@Convertor(params={@Parameter(name="format",value="%.0f")})
	String2StringConvertor stockCodeConvertor;
	
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;
	
	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(accountBean!=null?accountBean.maxStockCode:'', accountBean!=null?accountBean.maxStockMarket:'')}")
	StockBaseInfo stockInfoBean;
	@Override
	public void updateModel(Object value) {
		if (value instanceof GainBean) {
			registerBean("accountBean", value);
		}
	}
}
