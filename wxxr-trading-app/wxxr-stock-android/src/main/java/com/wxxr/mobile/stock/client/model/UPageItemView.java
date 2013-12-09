package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;


/**
 * 个人主页-挑战交易盘每个条目布局
 */
@View(name="uPageItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_page_item_layout")
public abstract class UPageItemView extends ViewBase implements IModelUpdater{

	
	GainBean accountBean;
	
	/**交易盘类型  0-模拟盘；1-实盘*/
	@Field(valueKey="enabled",binding="${accountBean!=null?accountBean.virtual==false?true:false:false}")
	boolean type;
	/**
	 * 股票名称
	 */
	@Field(valueKey="text", binding="${(stockInfoBean!=null&&stockInfoBean.name!=null)?stockInfoBean.name:'无持仓'}",
			attributes={@Attribute(name = "textColor", value = "${(accountBean.over!=null&&accountBean.over=='CLOSED')?'resourceId:color/gray':'resourceId:color/white'}")}
		   )
	String stock_name;
	
    /**
     * 股票代码
     */
	@Field(valueKey="text", binding="${(accountBean!=null&&accountBean.maxStockCode!=null)?accountBean.maxStockCode:'---'}", attributes={
			@Attribute(name="textColor",value="${(accountBean.over!=null&&accountBean.over=='CLOSED')?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String stock_code;
	
	/**
	 * 额度
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.sum:null}", converter="stock2SConvertor",
			attributes={@Attribute(name = "textColor", value = "${(accountBean.over!=null&&accountBean.over=='CLOSED')?'resourceId:color/gray':'resourceId:color/white'}")}
			)
	String challenge_amount;
	
	/**
	 * 盈亏
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.totalGain:null}", 
			attributes={@Attribute(name ="textColor", value = "${(accountBean.over!=null&&accountBean.over=='CLOSED') ? 'resourceId:color/gray': (accountBean.totalGain > 0 ? 'resourceId:color/red' : (accountBean.totalGain < 0 ? 'resourceId:color/green':'resourceId:color/white'))}")
			},
			converter="lossConvertor"
			)
	String profit_loss_amount;
	
	/**
	 * 交易时间
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.closeTime:'--'}", attributes={
			@Attribute(name="textColor",value="${(accountBean.over!=null&&accountBean.over=='CLOSED')?'resourceId:color/gray':'resourceId:color/white'}")
			})
	String trade_date;
	
	@Convertor(
			params={@Parameter(name="format", value="%.2f"),
					@Parameter(name="formatUnit",value="元"),
					@Parameter(name="multiple",value="100.0f"),
					@Parameter(name="nullString", value="--")
			}
			)
	StockLong2StringConvertor lossConvertor;
	
	@Convertor(
			params={
					@Parameter(name="format", value="%.0f"),
					@Parameter(name="multiple", value="1000000"),
					@Parameter(name="formatUnit",value="万"),
					@Parameter(name="nullString",value="--")
			
			}
			)
	StockLong2StringConvertor stock2SConvertor;
	
	
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
