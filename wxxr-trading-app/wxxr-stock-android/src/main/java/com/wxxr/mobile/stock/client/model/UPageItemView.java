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
import com.wxxr.mobile.stock.client.utils.Float2StringConvertor;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;


/**
 * 个人主页-挑战交易盘每个条目布局
 * @author renwenjie
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
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.maxStockName:'--'}",
			attributes={@Attribute(name = "textColor", value = "${(accountBean.over!=null&&accountBean.over=='CLOSED')?'resourceId:color/gray':'resourceId:color/white'}")}
		   )
	String stock_name;
	
    /**
     * 股票代码
     */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.maxStockCode:'--'}")
	String stock_code;
	
	/**
	 * 额度
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.sum:'--'}", converter="stock2SConvertor",
			attributes={@Attribute(name = "textColor", value = "${(accountBean.over!=null&&accountBean.over=='CLOSED')?'resourceId:color/gray':'resourceId:color/white'}")}
			)
	String challenge_amount;
	
	/**
	 * 盈亏
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.userGain:'--'}", 
			attributes={@Attribute(name ="textColor", value = "${(accountBean.over!=null&&accountBean.over=='CLOSED') ? 'resourceId:color/gray': (accountBean.userGain > 0 ? 'resourceId:color/red' : (accountBean.userGain < 0 ? 'resourceId:color/green':'resourceId:color/white'))}")
			},
			converter="f2SConvertor"
			)
	String profit_loss_amount;
	
	/**
	 * 交易时间
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.closeTime:'--'}", converter="lt2SConvertor")
	String trade_date;
	
	@Convertor(
			params={@Parameter(name="format", value="M月d日")}
			)
	LongTime2StringConvertor lt2SConvertor;
	
	@Convertor(
			params={@Parameter(name="format", value="%.2f"),
					@Parameter(name="formatUnit",value="元"),
					@Parameter(name="multiple",value="100.0f")
			}
			)
	StockLong2StringConvertor f2SConvertor;
	
	@Convertor(
			params={@Parameter(name="format", value="%.0f")
			}
			)
	StockLong2StringAutoUnitConvertor stock2SConvertor;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof GainBean) {
			registerBean("accountBean", value);
		}
	}
}
