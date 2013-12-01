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
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.ui.common.SimpleSelectionImpl;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

/**
 * 买入
 * 
 * @author duzhen
 * 
 */
@View(name = "BuyStockDetailPage", withToolbar = true, description="买入", provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.buy_stock_detail_layout")
public abstract class BuyStockDetailPage extends PageBase implements
		IModelUpdater,ISelectionChangedListener {
	
	@ViewGroup(viewIds={"StockQuotationView", "StockKLineView"})
	private IViewGroup contents;
	
	private static final Trace log = Trace.register(BuyStockDetailPage.class);
	private boolean hasShow = false;
	@Bean
	String acctIdBean;
	
	@Bean
	String marketBean;
	
	@Bean
	String nameBean;
	
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
	
	@Bean(type=BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getStockQuotation(codeBean, marketBean)}")
	StockQuotationBean stockQuotationBean;
	
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
	
	@Field(valueKey = "text", binding="${nameBean}${' '}${codeBean}")
	String stock;
	
	@Field(valueKey = "text", binding="${stockQuotationBean!=null?stockQuotationBean.newprice:''}", converter="stockLong2StringConvertor")
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
		hide();
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
		infoCenterService.getStockQuotation(codeBean, marketBean);
		return null;
	}
	
	@Command
	String handleMarketBtnClick(InputEvent envent) {
		orderPriceBean = stockQuotationBean.getClose()*1.1 + "";
		registerBean("orderPriceBean", stockQuotationBean.getClose()*1.1 + "");
		
		return null;
	}
	@Command
	String priceTextChanged(InputEvent event) {
		String key = (String) event.getProperty("changedText");
		try {
			String value = Float.parseFloat(key) * 100 + "";
			orderPriceBean = value;
			registerBean("orderPriceBean", value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Command
	String countTextChanged(InputEvent event) {
		String key = (String) event.getProperty("changedText");
		amountBean = key;
		registerBean("amountBean", key);
		return null;
	}
	
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		SimpleSelectionImpl impl = (SimpleSelectionImpl)selection;
		String[] stockInfos = (String[])impl.getSelected();
		this.codeBean = stockInfos[0];
		this.nameBean = stockInfos[1];
		this.marketBean = stockInfos[2];
		registerBean("codeBean", this.codeBean);
		registerBean("nameBean", this.nameBean);
		registerBean("marketBean", this.marketBean);
		infoCenterService.getStockQuotation(codeBean, marketBean);
		updateSelection((Object)stockInfos);
	}

	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			String code = "";
			String name = "";
			String market = "";
			String acctId = "";
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "code".equals(key)) {
					if(tempt instanceof String) {
						code = (String)tempt;
					}
					codeBean = code;
					registerBean("codeBean", code);
				} else if (tempt != null && "name".equals(key)) {
					if(tempt instanceof String) {
						name = (String)tempt;
					}
					nameBean = name;
					registerBean("nameBean", name);
				} else if (tempt != null && "market".equals(key)) {
					if(tempt instanceof String) {
						market = (String)tempt;
					}
					marketBean = market;
					registerBean("marketBean", market);
				} else if (tempt != null && "acctId".equals(key)) {
					if(tempt instanceof String) {
						acctId = (String)tempt;
					}
					acctIdBean = acctId;
					registerBean("acctIdBean", acctId);
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
	@Command(navigations = { @Navigation(on = "page", showView = "StockKLineView") })
	@ExeGuard(title="提示", message="正在获取数据，请稍后...", silentPeriod=1, cancellable=false)
	String handleBuyBtnClick(InputEvent event) {
			tradingService.buyStock(acctIdBean, marketBean, codeBean, orderPriceBean, amountBean);
		return "page";
	}
	
	@OnCreate
	void registerSelectionListener() {
		getUIContext().getWorkbenchManager().getWorkbench().getSelectionService().addSelectionListener("stockSearchPage", this);
	}
	
	@OnCreate
	protected void initStock() {
//		registerBean("acctIdBean", "11111");
//		registerBean("marketBean", "SH");
//		registerBean("nameBean", "无限新锐");
//		registerBean("codeBean", "600101");
		registerBean("marketPriceBean", "156");
		registerBean("amountBean", "");
		registerBean("fundBean", "10000000");
		
//		acctIdBean = "11111";
//		marketBean = "SH";
//		nameBean = "无限新锐";
//		codeBean = "600101";
		marketPriceBean = "156";
		amountBean = "";
		fundBean = "10000000";
		final Runnable[] tasks = new Runnable[1];
		tasks[0] = new Runnable() {

			@Override
			public void run() {
//				registerBean("orderPriceBean", marketPriceBean);

//				AppUtils.runOnUIThread(tasks[0], 10, TimeUnit.SECONDS);
			}
		};
		AppUtils.runOnUIThread(tasks[0], 6, TimeUnit.SECONDS);
		
	}
	
	@OnShow
	void startStockSearch() {
		if(hasShow) {
			if(StringUtils.isBlank(marketBean) || StringUtils.isBlank(codeBean)) {
				hide();
			} else {
				
			}
			return;
		}
		if(StringUtils.isBlank(marketBean) || StringUtils.isBlank(codeBean)) {
			AppUtils.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					getUIContext().getWorkbenchManager().getWorkbench().showPage("stockSearchPage", null, null);
					hasShow = true;
				}
			}, 100, TimeUnit.MILLISECONDS);
		}
	}
	
	@OnUIDestroy
	void destroyData() {
		hasShow = false;
		marketBean = null;
		codeBean = null;
	}
	
	@OnDestroy
	void removeSelectionListener() {
		getUIContext().getWorkbenchManager().getWorkbench().getSelectionService().removeSelectionListener("stockSearchPage", this);
	}
}
