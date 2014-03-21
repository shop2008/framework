package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.v2.bean.ChampionShipMessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.MessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.SignInMessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.TradingAccountMenuItem;

@View(name = "MainHomeItemView")
public abstract class MainHomeItemView extends ViewBase implements
		ItemViewSelector {

	@Override
	public String getItemViewId(Object itemData) {
		if (itemData instanceof TradingAccountMenuItem) {
			return "MainHomeItemTradingView";
		} else if (itemData instanceof SignInMessageMenuItem) {
			return "MainHomeItemSignInView";
		} else if (itemData instanceof ChampionShipMessageMenuItem) {
			return "MainHomeItemChampionShipView";
		} else if (itemData instanceof MessageMenuItem) {
			return "MainHomeItemMessageView";
		}
		return null;
	}

	@Override
	public String[] getAllViewIds() {
		return new String[] { "MainHomeItemTradingView",
				"MainHomeItemSignInView", "MainHomeItemChampionShipView",
				"MainHomeItemMessageView" };
	}
}
