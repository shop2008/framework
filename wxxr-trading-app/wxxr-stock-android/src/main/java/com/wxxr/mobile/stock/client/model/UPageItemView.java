package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.client.utils.Utils;


/**
 * 个人主页-挑战交易盘每个条目布局
 * @author renwenjie
 */
@View(name="uPageItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_page_item_layout")
public abstract class UPageItemView extends ViewBase implements IModelUpdater{

	
	TradingAccountBean accountBean;
	/**
	 * 股票名称
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.maxStockMarket:'--'}")
	String stock_name;
	
    /**
     * 股票代码
     */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.maxStockCode:'--'}")
	String stock_code;
	
	/**
	 * 额度
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.applyFee:'--'}")
	String challenge_amount;
	
	/**
	 * 盈亏
	 */
	@Field(valueKey="text", binding="${accountBean!=null?accountBean.totalGain:'--'}", attributes={@Attribute(name="textColor", value="${accountBean.totalGain>0?'resourceId:color/red':'resourceId:color/green'}")})
	String profit_loss_amount;
	
	/**
	 * 交易时间
	 */
	@Field(valueKey="text", binding="${accountBean!=null?util.getDate(accountBean.buyDay):'--'}")
	String trade_date;
	
	@Bean
	Utils util = Utils.getInstance();
	
	@OnShow
	protected void initData() {
		registerBean("util", util);
	}
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof TradingAccountBean) {
			registerBean("accountBean", value);
		}
	}
}
