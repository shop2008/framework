package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;

@View(name = "SellTradingItemView")
public abstract class SellTradingItemView extends ViewBase implements ItemViewSelector{
	
	@Override
	public String getItemViewId(Object value) {
		if (value instanceof StockTradingOrderBean) {
			StockTradingOrderBean order = (StockTradingOrderBean)value;
			
			if("PROCESSING".equals(order.getStatus()) || "100".equals(order.getStatus())) {
				return "TBuyTradingItemOrderView";
			} else {
				return "sellTradingStockOrder";
			}
		}
		return null;
	}
	
	@Override
	public String[] getAllViewIds() {
		return new String[] {"TBuyTradingItemOrderView","sellTradingStockOrder"};
	}

}
