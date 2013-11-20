/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

/**
 * 买入
 * 
 * @author duzhen
 * 
 */
@View(name = "BuyStockDetailPage", withToolbar = true, description="买入")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.activity_quick_buy")
public abstract class BuyStockDetailPage extends PageBase implements
		IModelUpdater {

	@Bean
	String stockCode;
	
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@OnShow
	protected void initStockInfo() {
	}

	
	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			String code = "";
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "result".equals(key)) {
					if(tempt instanceof Long) {
						code = (Long)tempt + "";
					} else if(tempt instanceof String) {
						code = (String)tempt;
					}
					registerBean("stockCode", code);
				}
			}
		}

	}
}
