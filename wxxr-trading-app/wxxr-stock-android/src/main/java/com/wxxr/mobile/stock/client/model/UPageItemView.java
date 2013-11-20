package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.GainBean;


/**
 * 个人主页-挑战交易盘每个条目布局
 * @author renwenjie
 */
@View(name="uPageItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_page_item_layout")
public abstract class UPageItemView extends ViewBase implements IModelUpdater{

	
	GainBean accountBean;
	/**
	 * 股票名称
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.maxStockName:'--'}")
	String stock_name;
	
    /**
     * 股票代码
     */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.maxStockCode:'--'}")
	String stock_code;
	
	/**
	 * 额度
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.sum:'--'}")
	String challenge_amount;
	
	/**
	 * 盈亏
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.userGain:'--'}", attributes={@Attribute(name="textColor", value="${accountBean.userGain>0?'resourceId:color/red':'resourceId:color/green'}")})
	String profit_loss_amount;
	
	/**
	 * 交易时间
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.closeTime:'--'}")
	String trade_date;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof GainBean) {
			registerBean("accountBean", value);
		}
	}
}
