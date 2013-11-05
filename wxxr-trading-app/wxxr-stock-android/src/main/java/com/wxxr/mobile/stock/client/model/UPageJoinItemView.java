package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;

/**
 * 个人主页-参赛交易盘每个条目布局
 * @author renwenjie
 */
@View(name="join_item_view")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_page_item_layout")
public abstract class UPageJoinItemView extends ViewBase {
	/**
	 * 股票名称
	 */
	@Field(valueKey="text")
	String stock_name;
	
    /**
     * 股票代码
     */
	@Field(valueKey="text")
	String stock_code;
	
	/**
	 * 额度
	 */
	@Field(valueKey="text")
	String challenge_amount;
	
	/**
	 * 盈亏
	 */
	@Field(valueKey="text")
	String profit_loss_amount;
	
	/**
	 * 交易时间
	 */
	@Field(valueKey="text")
	String trade_date;
}
