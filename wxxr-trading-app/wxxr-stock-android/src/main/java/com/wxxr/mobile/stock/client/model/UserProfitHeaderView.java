package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="UserProfitHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_profit_header_view")
public abstract class UserProfitHeaderView extends ViewBase {

	@Field(valueKey="text")
	String totalMoneyProfit;
	
	@Field(valueKey="text")
	String totalScoreProfit;
}
