package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="SellTDTradingPageHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.sell_td_page_header_view")
public abstract class SellTDTradingPageHeaderView extends ViewBase implements IModelUpdater {

	@Override
	public void updateModel(Object value) {

	}
}
