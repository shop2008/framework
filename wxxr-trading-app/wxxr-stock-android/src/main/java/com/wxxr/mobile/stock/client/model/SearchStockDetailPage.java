/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

/**
 * 模拟盘详情
 * 
 * @author duzhen
 * 
 */
@View(name = "SearchStockDetailPage", withToolbar = true, description="个股界面")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.gegu_page_layout")
public abstract class SearchStockDetailPage extends PageBase implements
		IModelUpdater {

//	@Bean
//	String idBean;
//	@Bean
//	String buyDayBean;
//	@Bean
//	String sellDayBean;
//	@Bean
//	String applyFeeBean;
//	@Bean
//	String lossLimitBean;
//
//	@Field(valueKey = "text", binding = "${idBean}")
//	String id;
//
//	@Field(valueKey = "text", binding = "${buyDayBean}")
//	String buyDay;
//
//	@Field(valueKey = "text", binding = "${sellDayBean}")
//	String sellDay;
//
//	@Field(valueKey = "text", binding = "${applyFeeBean}")
//	String applyFee;
//
//	@Field(valueKey = "text", binding = "${lossLimitBean}")
//	String lossLimit;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@OnShow
	protected void initStockInfo() {
//		registerBean("idBean", "--");
//		registerBean("buyDayBean", "--");
//		registerBean("sellDayBean", "--");
//		registerBean("applyFeeBean", "--");
//		registerBean("lossLimitBean", "--");
	}

	
	@Override
	public void updateModel(Object data) {

		if (data instanceof Map) {
//			Map result = (Map) data;
//			for (Object key : result.keySet()) {
//				Object tempt = (Object) result.get(key);
//
//				if ("id".equals(key)) {
//					if (tempt != null)
//						registerBean("idBean", tempt);
//					else
//						registerBean("idBean", "");
//				} else if ("buyDay".equals(key)) {
//					if (tempt != null)
//						registerBean("buyDayBean", tempt);
//					else
//						registerBean("buyDayBean", "");
//				} else if ("sellDay".equals(key)) {
//					if (tempt != null)
//						registerBean("sellDayBean", tempt);
//					else
//						registerBean("sellDayBean", "");
//				} else if ("applyFee".equals(key)) {
//					if (tempt != null)
//						registerBean("applyFeeBean", tempt);
//					else
//						registerBean("applyFeeBean", "");
//				} else if ("lossLimit".equals(key)) {
//					if (tempt != null)
//						registerBean("lossLimitBean", tempt);
//					else
//						registerBean("lossLimitBean", "");
//				}
//			}
		}
	}
}
