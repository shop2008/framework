package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.GainBean;

@View(name="TradeRecordItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.trade_record_item_layout")
public abstract class TradeRecordItemView extends ViewBase implements
		IModelUpdater {

	
	/**
	 * 股票名称
	 */
	@Field(valueKey="text", binding="${bean!=null?bean.stockName:null}")
	String stock_name;
	
    /**
     * 股票代码
     */
	@Field(valueKey="text", binding="${bean!=null?bean.stockCode:null}")
	String stock_code;
	
	/**
	 * 额度
	 */
	@Field(valueKey="text", binding="${bean!=null?bean.sum:null}"
			)
	String challenge_amount;
	
	/**
	 * 盈亏
	 */
	@Field(valueKey="text", binding="${bean!=null?bean.userGain:null}"
			)
	String profit_loss_amount;
	
	/**
	 * 交易时间
	 */
	@Field(valueKey="text", binding="${bean!=null?bean.closeTime:'--'}")
	String trade_date;
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof GainBean) {
			GainBean bean = (GainBean) value;
			registerBean("bean", bean);
		}
	}


}
