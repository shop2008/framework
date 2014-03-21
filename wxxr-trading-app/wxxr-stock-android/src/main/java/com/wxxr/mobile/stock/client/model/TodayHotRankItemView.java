package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="todayHotRankItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.today_hot_rank_item_layout")
public abstract class TodayHotRankItemView extends ViewBase {

	@Field(valueKey="text")
	String buyNum;
	
	@Field(valueKey="text")
	String stockName;
	
	@Field(valueKey="text")
	String curPrice;
	
	@Field(valueKey="text")
	String buyStockNum;
	
}
