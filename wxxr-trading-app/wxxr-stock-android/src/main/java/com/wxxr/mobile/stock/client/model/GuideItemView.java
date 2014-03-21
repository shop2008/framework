package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name = "GuideItemView")
public abstract class GuideItemView extends ViewBase implements ItemViewSelector{
	
	@Override
	public String getItemViewId(Object value) {
		if (value instanceof String) {
			if("resourceId:drawable/guide_10".equals(value)) {
				return "GuideVoucherItemView";
			} else {
				return "GuideSwiperItemView";
			}
		}
		return null;
	}
	
	@Override
	public String[] getAllViewIds() {
		return new String[] {"TBuyTradingItemOrderView","sellTradingStockOrder"};
	}

}
