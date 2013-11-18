package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IAppToolbar;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;

/**
 * @author duzhen
 */
@View(name = "TBuyTradingPage", withToolbar = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.buy_trading_account_info_page_layout")
public abstract class TBuyTradingPage extends PageBase implements IModelUpdater {

	private static final Trace log = Trace.register(TBuyTradingPage.class);

	@Bean
	String stockId;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${tradingService.getTradingAccountInfo(stockId)}")
	TradingAccountBean tradingBean;
	// 字段
	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.buyDay:'--'}${'买入'}")
	String buyDay;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.sellDay:'--'}${'卖出'}")
	String sellDay;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.applyFee:'--'}${'万'}")
	String applyFee;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.avalibleFee:'--'}${'万'}")
	String avalibleFee;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.gainRate:'--'}${'%'}")
	String gainRate;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.totalGain:'--'}${'元'}")
	String totalGain;

	@Field(valueKey = "options", binding = "${tradingBean != null ? tradingBean.tradingOrders : null}")
	List<StockTradingOrderBean> tradingOrders;

	@Field(valueKey = "visible", visibleWhen = "${tradingBean != null ? (tradingBean.tradingOrders != null?(tradingBean.tradingOrders.size() > 0 ? false : true):false) : false}")
	boolean noOrders;

	@Field(valueKey = "text")
	String buyBtn;

	@Menu(items = { "left", "right" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	/**
	 * 订单详情点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "right", label = "交易详情", icon = "resourceId:drawable/jyjl") }, navigations = { @Navigation(on = "*", showPage = "stockSearchPage") })
	CommandResult toolbarClickedRight(InputEvent event) {
		CommandResult resutl = new CommandResult();
		Long stockId = 0L;
		if (event.getProperty("position") instanceof Integer) {
			int position = (Integer) event.getProperty("position");
			if (tradingOrders != null && tradingOrders.size() > 0) {
				StockTradingOrderBean tempTradingA = tradingOrders
						.get(position);
				stockId = tempTradingA.getId();
			}
		}
		resutl.setResult("stockSearchPage");
		resutl.setPayload(stockId);
		return resutl;
	}

	@OnShow
	void initTitleBar() {
		registerBean("stockId", stockId);
		getPageToolbar().setTitle("模拟盘", null);
		// ((IStockAppToolbar)getAppToolbar()).setTitle("模拟盘", null);
	}

	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Long tempt = (Long) temp.get(key);
				if (tempt != null && "result".equals(key)) {
					stockId = tempt + "";
				}
			}
		}

	}

	@Command
	String handleTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleTMegaTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		tradingService.getTradingAccountInfo(stockId);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}

	/**
	 * 模拟盘点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "TBuyStockInfoPage", showPage = "TBuyStockInfoPage") })
	CommandResult handleStockClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			Map map = new HashMap();
			if (tradingBean != null) {
				map.put("id", tradingBean.getId());
				map.put("buyDay", tradingBean.getBuyDay());
				map.put("sellDay", tradingBean.getSellDay());
				map.put("applyFee", tradingBean.getApplyFee());
				map.put("lossLimit", tradingBean.getLossLimit());
			}
			resutl.setResult("TBuyStockInfoPage");
			resutl.setPayload(map);
			return resutl;
		}

		return null;
	}

	/**
	 * 订单列表点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "stockSearchPage", showPage = "stockSearchPage") })
	CommandResult handleItemClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			Long stockId = 0L;
			if (event.getProperty("position") instanceof Integer) {
				int position = (Integer) event.getProperty("position");
				if (tradingOrders != null && tradingOrders.size() > 0) {
					StockTradingOrderBean tempTradingA = tradingOrders
							.get(position);
					stockId = tempTradingA.getId();
				}
			}
			resutl.setResult("stockSearchPage");
			resutl.setPayload(stockId);
			return resutl;
		}
		return null;
	}

	/**
	 * 购买点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showPage = "stockSearchPage") })
	String handleBuyBtnClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			Long stockId = 0L;
			// if (event.getProperty("position") instanceof Integer) {
			// int position = (Integer) event.getProperty("position");
			// if (tradingOrders != null && tradingOrders.size() > 0) {
			// StockTradingOrderBean tempTradingA = tradingOrders.get(position);
			// stockId = tempTradingA.getId();
			// }
			// }
			resutl.setResult("stockSearchPage");
			resutl.setPayload(stockId);
			// return resutl;
		}
		return "";
	}

	@Override
	public void onToolbarCreated(IAppToolbar toolbar) {
		// TODO Auto-generated method stub
		super.onToolbarCreated(toolbar);
		toolbar.setTitle("模拟盘", null);
	}
}
