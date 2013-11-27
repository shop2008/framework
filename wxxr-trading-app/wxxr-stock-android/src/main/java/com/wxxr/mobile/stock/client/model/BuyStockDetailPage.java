/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.mobile.stock.client.widget.IStockSelectedCallBack;

/**
 * 买入
 * 
 * @author duzhen
 * 
 */
@View(name = "BuyStockDetailPage", withToolbar = true, description="买入")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.buy_stock_detail_layout")
public abstract class BuyStockDetailPage extends PageBase implements
		IModelUpdater, IStockSelectedCallBack {
	private static final Trace log = Trace.register(BuyStockDetailPage.class);
	
	@Bean
	String acctIdBean;
	
	@Bean
	String marketBean;
	
	@Bean
	String stockBean;
	
	@Bean
	String codeBean;
	
	@Bean
	String marketPriceBean;
	
	@Bean
	String amountBean;
	
	@Bean
	String fundBean;
	
	@Bean
	String orderPriceBean;
	
//	@Bean
//	String orderCountBean;
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertor;
	
	@Field(valueKey = "text", attributes = {
			@Attribute(name = "orderPrice", value = "${orderPriceBean}"),
			@Attribute(name = "marketPrice", value = "${marketPriceBean}"),
			@Attribute(name = "fund", value = "${fundBean}") })
	String inputView;
	
	@Field(valueKey = "text", binding="${stockBean}${' '}${codeBean}")
	String stock;
	
	@Field(valueKey = "text", binding="${orderPriceBean}")
	String price;
	
	@Field(valueKey = "text", binding="${amountBean}")
	String count;
	
	@Field(valueKey = "text")
	String refresh;
	
	@Field(valueKey = "text")
	String buyBtn;
	
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	@Command(navigations = {@Navigation(on = "stockSearchPage", showPage = "stockSearchPage")})
	CommandResult handlerStockClicked(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult result = new CommandResult();
			result.setResult("stockSearchPage");
			result.setPayload(this);
			return result;
		}
		return null;
	}
	
	@Command
	String handlerRefreshClicked(InputEvent event) {
		registerBean("orderPriceBean", marketPriceBean);
		return null;
	}
	
	@Command
	String priceTextChanged(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String key = (String) event.getProperty("changedText");
			registerBean("orderPriceBean", key);
		}
		return null;
	}
	
	@Override
	public void stockSelected(String code, String name) {
		log.debug("handlerStockClicked get String : "+code + " name : " + name);
		registerBean("codeBean", code);
		registerBean("stockBean", name);
	}
	
	@OnCreate
	protected void initStock() {
		registerBean("acctIdBean", "11111");
		registerBean("marketBean", "SH");
		registerBean("stockBean", "无限新锐");
		registerBean("codeBean", "600101");
		registerBean("marketPriceBean", "156");
		registerBean("amountBean", "");
		registerBean("fundBean", "100000");
		
		acctIdBean = "11111";
		marketBean = "SH";
		stockBean = "无限新锐";
		codeBean = "600101";
		marketPriceBean = "156";
		amountBean = "";
		fundBean = "100000";
		final Runnable[] tasks = new Runnable[1];
		tasks[0] = new Runnable() {

			@Override
			public void run() {
				registerBean("orderPriceBean", marketPriceBean);

//				AppUtils.runOnUIThread(tasks[0], 10, TimeUnit.SECONDS);
			}
		};
		AppUtils.runOnUIThread(tasks[0], 6, TimeUnit.SECONDS);
		
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
			tradingService.buyStock(acctIdBean, marketBean, codeBean, orderPriceBean, amountBean);
			return "";
		}
		return null;
	}
}
