/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

/**
 * 买入
 * 
 * @author duzhen
 * 
 */
@View(name = "BuyStockDetailPage", withToolbar = true, description="买入")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.buy_stock_detail_layout")
public abstract class BuyStockDetailPage extends PageBase implements
		IModelUpdater {

	@Bean
	String acctIdBean;
	
	@Bean
	String marketBean;
	
	@Bean
	String codeBean;
	
	@Bean
	String priceBean;
	
	@Bean
	String amountBean;
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Field(valueKey = "text")
	String buyBtn;
	
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@OnShow
	protected void initStockInfo() {
		registerBean("acctIdBean", "11111");
		registerBean("marketBean", "SH");
		registerBean("codeBean", "600101");
		registerBean("priceBean", "1.56");
		registerBean("amountBean", "6000");
		
		acctIdBean = "11111";
		marketBean = "SH";
		codeBean = "600101";
		priceBean = "1.56";
		amountBean = "6000";
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
	
	/**
	 * 买入点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showPage = "StockQuotationView") })
	@ExeGuard(title="提示", message="正在获取数据，请稍后...", silentPeriod=1, cancellable=false)
	String handleBuyBtnClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			tradingService.buyStock(acctIdBean, marketBean, codeBean, priceBean, amountBean);
			return "";
		}
		return null;
	}
}
