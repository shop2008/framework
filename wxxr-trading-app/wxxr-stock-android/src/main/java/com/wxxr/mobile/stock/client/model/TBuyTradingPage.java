package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.event.api.IBroadcastEvent;
import com.wxxr.mobile.core.event.api.IEventListener;
import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.event.NewRemindingMessagesEvent;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.Constants;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.SpUtil;
import com.wxxr.mobile.stock.client.utils.Utils;

/**
 * @author duzhen
 */
@View(name = "TBuyTradingPage", withToolbar = true, description = "交易盘", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.buy_trading_account_info_page_layout")
public abstract class TBuyTradingPage extends PageBase implements
		IModelUpdater, IEventListener {

	private static final Trace log = Trace.register(TBuyTradingPage.class);

	@Bean
	boolean isSelf = true; // true自己，false别人

	@Bean
	boolean isVirtual = true; // true模拟盘，false实盘
	// 消息推送
	@Field(valueKey = "text")
	String message;
	DataField<String> messageField;

	@Field(valueKey = "visible")
	boolean messageLayout;
	DataField<Boolean> messageLayoutField;

	@Field(valueKey = "text")
	String closeBtn;

	@Bean
	String acctId;

	@Convertor(params = { @Parameter(name = "format", value = "M月d日买入"),
			@Parameter(name = "nullString", value = "-月-日买入") })
	LongTime2StringConvertor longTime2StringConvertorBuy;

	@Convertor(params = { @Parameter(name = "format", value = "M月d日卖出"),
			@Parameter(name = "nullString", value = "-月-日卖出") })
	LongTime2StringConvertor longTime2StringConvertorSell;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${tradingService.getTradingAccountInfo(acctId)}")
	TradingAccountBean tradingBean;
	// 字段
	@Field(valueKey = "text", binding = "${tradingBean!=null?(tradingBean.buyDay==0?'-1':tradingBean.buyDay):'-1'}", converter = "longTime2StringConvertorBuy")
	String buyDay;

	@Field(valueKey = "text", binding = "${tradingBean!=null?(tradingBean.sellDay==0?'-1':tradingBean.sellDay):'-1'}", converter = "longTime2StringConvertorSell")
	String sellDay;

	@Field(valueKey = "options", binding = "${tradingBean != null ? tradingBean.tradingOrders : null}")
	List<StockTradingOrderBean> tradingOrders;

	@Field(valueKey = "visible", visibleWhen = "${tradingBean != null ? (tradingBean.tradingOrders != null?(tradingBean.tradingOrders.size() > 0 ? false : true):false) : false}")
	boolean noOrders;

	@Field(valueKey = "text", visibleWhen = "${isSelf}", enableWhen = "${tradingBean!=null}", attributes = { @Attribute(name = "backgroundImageURI", value = "${isVirtual?'resourceId:drawable/blue_button_style':'resourceId:drawable/red_button_style'}") })
	String buyBtn;

	@Field(attributes = {
			@Attribute(name = "enablePullDownRefresh", value = "true"),
			@Attribute(name = "enablePullUpRefresh", value = "false") })
	String acctRefreshView;

	@Field(valueKey = "text", attributes = { @Attribute(name = "backgroundImageURI", value = "${isVirtual?'resourceId:drawable/c_t_day':'resourceId:drawable/t_t_day'}") })
	String titleBg;

	@Menu(items = { "left", "right" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	@OnShow
	void registerEventListener() {
		AppUtils.getService(IEventRouter.class).registerEventListener(
				NewRemindingMessagesEvent.class, this);
	}

	@Override
	public void onEvent(IBroadcastEvent event) {
		if (tradingService != null)
			tradingService.getTradingAccountInfo(acctId);
//		if (!AppUtils.getService(IUserManagementService.class)
//				.getPushMessageSetting())
//			return;
		NewRemindingMessagesEvent e = (NewRemindingMessagesEvent) event;
		final RemindMessageBean[] messages = e.getReceivedMessages();

		final int[] count = new int[1];
		count[0] = 0;
		final Runnable[] tasks = new Runnable[1];
		tasks[0] = new Runnable() {

			@Override
			public void run() {
				if (messages != null && count[0] < messages.length) {
					RemindMessageBean msg = messages[count[0]++];
					String time = Utils.getCurrentTime("HH:mm");
					messageField.setValue(time + ", " + msg.getTitle() + ", "
							+ msg.getContent());
					messageLayoutField.setValue(true);
					AppUtils.runOnUIThread(tasks[0], 6, TimeUnit.SECONDS);
				}
			}
		};
		if (messages != null)
			AppUtils.runOnUIThread(tasks[0], 5, TimeUnit.SECONDS);
	}

	@OnHide
	void unRegisterEventListener() {
		messageLayoutField.setValue(false);
		AppUtils.getService(IEventRouter.class).unregisterEventListener(
				NewRemindingMessagesEvent.class, this);
	}

	/**
	 * 推送信息关闭
	 * 
	 * @param event
	 * @return
	 */
	@Command
	String handleCloseBtnClick(InputEvent event) {
		messageLayoutField.setValue(false);
		return "";
	}

	/**
	 * 交易详情点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "right", label = "交易详情", icon = "resourceId:drawable/message_button_style") }, navigations = { @Navigation(on = "*", showPage = "TradingRecordsPage") })
	CommandResult toolbarClickedRight(InputEvent event) {
		CommandResult resutl = new CommandResult();
		resutl.setResult("TradingRecordsPage");
		resutl.setPayload(this.acctId);
		return resutl;
	}

	@OnShow
	void initTitleBar() {
		if (isVirtual) {
			getPageToolbar().setTitle("参赛交易盘", null);
		} else {
			getPageToolbar().setTitle("挑战交易盘T+1", null);
		}
	}

	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && Constants.KEY_ACCOUNT_ID_FLAG.equals(key)) {
					if (tempt instanceof Long) {
						acctId = (Long) tempt + "";
					} else if (tempt instanceof String) {
						acctId = (String) tempt;
					}
					registerBean("acctId", acctId);
				} else if (tempt != null
						&& Constants.KEY_VIRTUAL_FLAG.equals(key)) {
					if (tempt instanceof Boolean) {
						isVirtual = (Boolean) tempt;
					}
					registerBean("isVirtual", isVirtual);
				} else if (tempt != null && Constants.KEY_SELF_FLAG.equals(key)) {
					if (tempt instanceof Boolean) {
						isSelf = (Boolean) tempt;
					}
					registerBean("isSelf", isSelf);
				}
			}
		}

	}

	@Command
	String handleRefresh(InputEvent event) {
		if("TopRefresh".equals(event.getEventType())) {
			if (log.isDebugEnabled()) {
				log.debug("TBuyTradingPage : handleTopRefresh");
			}
			tradingService.getSyncTradingAccountInfo(acctId);
			// registerBean("tradingBean", tradingBean);
		}
		return null;
	}

	/**
	 * 列表点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = {
			@Navigation(on = "BuyStockDetailPage", showPage = "BuyStockDetailPage"),
			@Navigation(on = "GeGuStockPage", showPage = "GeGuStockPage") })
	CommandResult handleItemClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())) {
			CommandResult result = new CommandResult();

			HashMap<String, Object> map = new HashMap<String, Object>();
			if (event.getProperty("position") instanceof Integer) {
				int position = (Integer) event.getProperty("position");
				List<StockTradingOrderBean> orders = (tradingBean != null ? tradingBean
						.getTradingOrders() : null);
				String code = "";
				String name = "";
				String market = "";
				String avalible = "";
				if (orders != null && orders.size() > 0) {
					StockTradingOrderBean bean = orders.get(position);
					code = bean.getStockCode();
					name = bean.getStockName();
					market = bean.getMarketCode();
					avalible = tradingBean.getAvalibleFee() + "";

					map.put(Constants.KEY_CODE_FLAG, code);
					map.put(Constants.KEY_NAME_FLAG, name);
					map.put(Constants.KEY_MARKET_FLAG, market);
					map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
					map.put("acctId", acctId);
					map.put("avalible", avalible);
				}
				if (isSelf) {
					result.setResult("BuyStockDetailPage");
					result.setPayload(map);
					updateSelection(new StockSelection(market, code, name, 1));
				} else {
					result.setResult("GeGuStockPage");
					result.setPayload(map);
					// 个股的viewPager使用
					updateSelection(new StockSelection(market, code, name));
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * 买入点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "BuyStockDetailPage", showPage = "BuyStockDetailPage") })
	CommandResult handleBuyBtnClick(InputEvent event) {
		CommandResult result = new CommandResult();
		HashMap<String, Object> map = new HashMap<String, Object>();
		String avalible = tradingBean.getAvalibleFee() + "";
		map.put("acctId", acctId);
		map.put("avalible", avalible);
		map.put(Constants.KEY_VIRTUAL_FLAG, isVirtual);
		result.setResult("BuyStockDetailPage");
		result.setPayload(map);
		updateSelection(new StockSelection()); //清空之前各种stockPage update出来的数据
		return result;
	}

	@OnUIDestroy
	void destroyData() {
		SpUtil.getInstance(AppUtils.getFramework().getAndroidApplication()).save(Constants.KEY_CANCEL_ORDERS, "");
	}
}
