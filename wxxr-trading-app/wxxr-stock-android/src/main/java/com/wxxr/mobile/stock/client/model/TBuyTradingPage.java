package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
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
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

/**
 * @author duzhen
 */
@View(name = "TBuyTradingPage", withToolbar = true, description="模拟盘/实盘")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.buy_trading_account_info_page_layout")
public abstract class TBuyTradingPage extends PageBase implements IModelUpdater {

	private static final Trace log = Trace.register(TBuyTradingPage.class);

	@Bean
	String acctId;

	@Convertor(params={
			@Parameter(name="format",value="M月d日买入")
	})
	LongTime2StringConvertor longTime2StringConvertorBuy;
	
	@Convertor(params={
			@Parameter(name="format",value="M月d日卖出")
	})
	LongTime2StringConvertor longTime2StringConvertorSell;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorYuan;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertorInt;
	
	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${tradingService.getTradingAccountInfo(stockId)}")
	TradingAccountBean tradingBean;
	// 字段
	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.buyDay:'-1'}", converter = "longTime2StringConvertorBuy")
	String buyDay;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.sellDay:'-1'}", converter = "longTime2StringConvertorSell")
	String sellDay;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.applyFee:''}", converter = "stockLong2StringAutoUnitConvertorInt")
	String applyFee;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.avalibleFee:''}", converter = "stockLong2StringAutoUnitConvertor")
	String avalibleFee;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.gainRate:''}", converter = "stockLong2StringConvertorSpecial", attributes={
			@Attribute(name = "textColor", value = "${tradingBean==null?'resourceId:color/gray':tradingBean.gainRate>0?'resourceId:color/red':(tradingBean.gainRate<0?'resourceId:color/green':'resourceId:color/gray')}")
			})
	String gainRate;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.totalGain:''}", converter = "stockLong2StringConvertorYuan", attributes={
			@Attribute(name = "textColor", value = "${tradingBean==null?'resourceId:color/gray':tradingBean.totalGain>0?'resourceId:color/red':(tradingBean.totalGain<0?'resourceId:color/green':'resourceId:color/gray')}")
			})
	String totalGain;

	@Field(valueKey = "options", binding = "${tradingBean != null ? tradingBean.tradingOrders : null}")
	List<StockTradingOrderBean> tradingOrders;

	@Field(valueKey = "visible", visibleWhen = "${tradingBean != null ? (tradingBean.tradingOrders != null?(tradingBean.tradingOrders.size() > 0 ? false : true):false) : false}")
	boolean noOrders;

	@Field(valueKey = "text")
	String buyBtn;

	@Field(attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
						@Attribute(name = "enablePullUpRefresh", value= "false")})
	String acctRefreshView;
	
	@Menu(items = { "left", "right" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	/**
	 * 交易详情点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "right", label = "交易详情", icon = "resourceId:drawable/jyjl") }, 
			navigations = { @Navigation(on = "*", showPage = "TradingRecordsPage") })
	CommandResult toolbarClickedRight(InputEvent event) {
		CommandResult resutl = new CommandResult();
		Long stockId = 0L;
		if (tradingBean != null) {
			stockId = tradingBean.getId();
		}
		resutl.setResult("TradingRecordsPage");
		resutl.setPayload(stockId);
		return resutl;
	}

	@OnShow
	void initTitleBar() {
		registerBean("stockId", acctId);
		getPageToolbar().setTitle("模拟盘", null);
		// ((IStockAppToolbar)getAppToolbar()).setTitle("模拟盘", null);
	}

	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "result".equals(key)) {
					if(tempt instanceof Long) {
						acctId = (Long)tempt + "";
					} else if(tempt instanceof String) {
						acctId = (String)tempt;
					}
					registerBean("stockId", acctId);
				}
			}
		}

	}

	@Command
	String handleTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("TBuyTradingPage : handleTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		tradingService.getTradingAccountInfo(acctId);
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
			if (tradingBean != null) {
				resutl.setPayload(tradingBean.getId());
			}
			resutl.setResult("TBuyStockInfoPage");
			return resutl;
		}

		return null;
	}

	/**
	 * 列表点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "BuyStockDetailPage", showPage = "BuyStockDetailPage") })
	CommandResult handleItemClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (event.getProperty("position") instanceof Integer) {
				int position = (Integer) event.getProperty("position");
				List<StockTradingOrderBean> orders = (tradingBean != null ? tradingBean
						.getTradingOrders() : null);
				if (orders != null && orders.size() > 0) {
					StockTradingOrderBean bean = orders.get(position);
					String code = bean.getStockCode();
					String name = bean.getStockName();
					map.put("code", code);
					map.put("name", name);
				}
			}
			resutl.setResult("BuyStockDetailPage");
			resutl.setPayload(map);
			return resutl;
		}
		return null;
	}

	/**
	 * 买入点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "*", showPage = "BuyStockDetailPage") })
	String handleBuyBtnClick(InputEvent event) {
		return null;
	}

	@Override
	public void onToolbarCreated(IAppToolbar toolbar) {
		// TODO Auto-generated method stub
		super.onToolbarCreated(toolbar);
		toolbar.setTitle("模拟盘", null);
	}
}
