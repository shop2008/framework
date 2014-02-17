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
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.event.NewRemindingMessagesEvent;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.Constants;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.Utils;

@View(name="sellTradingAccount",withToolbar=true, description="实盘/模拟",provideSelection=true)
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY,layoutId="R.layout.sell_trading_account_info_page_layout")
public abstract class SellTradingAccountPage extends PageBase implements IModelUpdater , IEventListener  {

	static Trace log = Trace.getLogger(SellTradingAccountPage.class);
	
	@Menu(items={"left","right"})
	private IMenu toolbar;
	
 
	@Command(description="Invoke when a toolbar item was clicked",uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button_style")
			}
	)
	String toolbarClickedLeft(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Toolbar item :left was clicked !");
		}
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}	
	
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getTradingAccountInfo(accid)}")
	TradingAccountBean tradingAccount;
	
	@Convertor(params={
			@Parameter(name="format",value="M月d日买入"),
			@Parameter(name="nullString",value="-月-日买入")
	})
	LongTime2StringConvertor longTime2StringConvertorBuy;
	
	@Convertor(params={
			@Parameter(name="format",value="M月d日卖出"),
			@Parameter(name="nullString",value="-月-日卖出")
	})
	LongTime2StringConvertor longTime2StringConvertorSell;	

	/** 交易盘编号*/
	private long id;
	
	@Bean
	boolean enabled;
	
	/**买入日期  */
	@Field(valueKey="text",binding="${tradingAccount!=null?(tradingAccount.buyDay==0?'-1':tradingAccount.buyDay):'-1'}", converter = "longTime2StringConvertorBuy")
	String buyDay;  
	
	/**卖出日期 */
	@Field(valueKey="text",binding="${tradingAccount!=null?(tradingAccount.sellDay==0?'-1':tradingAccount.sellDay):'-1'}", converter = "longTime2StringConvertorSell")
	String sellDay;  
	
	/**交易订单列表*/
	@Field(valueKey="options",binding="${tradingAccount!=null?tradingAccount.tradingOrders:null}",enableWhen="${tradingAccount.over!='CLEARING'}")
	List<StockTradingOrderBean> stockTradingOrder;
	/**交易订单
	 * 为空：按钮不可用
	 * 非空：按钮可用
	 * */
	@Field(valueKey="enabled",enableWhen="${(tradingAccount.tradingOrders!=null && tradingAccount.tradingOrders.size()>0 && tradingAccount.over!='CLEARING')}")
	boolean isRedSellBtn;
	
	@Field(valueKey="enabled",enableWhen="${(tradingAccount.tradingOrders!=null && tradingAccount.tradingOrders.size()>0 && tradingAccount.over!='CLEARING')}")
	boolean isRedCleanBtn;
	
	@Field(valueKey="enabled",enableWhen="${(tradingAccount.tradingOrders!=null && tradingAccount.tradingOrders.size()>0 && tradingAccount.over!='CLEARING')}")
	boolean isBlueSellBtn;
	
	@Field(valueKey="enabled",enableWhen="${(tradingAccount.tradingOrders!=null && tradingAccount.tradingOrders.size()>0 && tradingAccount.over!='CLEARING')}")
	boolean isBlueCleanBtn;
	
	@Field(valueKey="enabled",enableWhen="${virtual}")
	boolean dateTitle;
	@Bean
	String accid;
	@Bean
	boolean isSelf = true;	
	@Bean
	int isBtnState = 0;
	
	@Field(valueKey="visible",visibleWhen="${isSelf && isBtnState==0}")
	boolean blueBtn;
	
	@Field(valueKey="visible",visibleWhen="${isSelf && isBtnState==1}")
	boolean redBtn;
	
	
	@Field(valueKey="text", attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String acctRefreshView;
	
	/**交易盘状态 CLOSED-已结算；UNCLOSE-未结算,CLEARING-正在结算*/
	private String over;
	
	/**是否为模拟盘*/
	@Bean
	boolean virtual;
	
	/**1:表示T日交易盘,0:T+1日交易盘*/
	private int status;	
	
	
	String tradingTitle = "模拟盘";
	
	//消息推送
	@Field(valueKey = "text")
	String message;
	DataField<String> messageField;
	
	@Field(valueKey = "visible")
	boolean messageLayout;
	DataField<Boolean> messageLayoutField;
	
	@Field(valueKey = "text")
	String closeBtn;
	
	@Bean
	Utils utils = Utils.getInstance();
	
	@OnShow
	void initData(){
		if(this.virtual){
			getAppToolbar().setTitle("参赛交易盘", null);
		}else{
			getAppToolbar().setTitle("挑战交易盘T+1", null);
		}
		AppUtils.getService(IEventRouter.class).registerEventListener(NewRemindingMessagesEvent.class, this);
		
		if( tradingAccount != null && tradingAccount.getTradingOrders() != null && tradingAccount.getTradingOrders().size()>0){
			enabled_view = true;
		}
		registerBean("enabled_view", enabled_view);
	}
	
	
	@Override
	public void onEvent(IBroadcastEvent event) {
		if(tradingService != null)
			tradingService.getTradingAccountInfo(accid);
		if(!(AppUtils.getService(IUserManagementService.class).getMyUserInfo()!=null?AppUtils.getService(IUserManagementService.class).getMyUserInfo().getMessagePushSettingOn():false))
			return;
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
					messageField.setValue(msg.getCreatedDate() + "，"
							+ msg.getTitle() + "，" + msg.getContent());
					messageLayoutField.setValue(true);
					AppUtils.runOnUIThread(tasks[0], 5, TimeUnit.SECONDS);
				}
			}
		};
		if (messages != null)
			AppUtils.runOnUIThread(tasks[0], 5, TimeUnit.SECONDS);
	}
	
	@OnHide
	void unRegisterEventListener() {
		messageLayoutField.setValue(false);
		AppUtils.getService(IEventRouter.class).unregisterEventListener(NewRemindingMessagesEvent.class, this);
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
	 * 订单详情点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "right", label = "交易详情", icon = "resourceId:drawable/message_button_style") }, 
			navigations = { @Navigation(on = "*", showPage = "TradingRecordsPage")
			})
	CommandResult toolbarClickedRight(InputEvent event) {
		CommandResult resutl = new CommandResult();
		if(this.accid!=null){
			resutl.setResult("TradingRecordsPage");
			resutl.setPayload(this.accid);
			return resutl;
		}
		return null;
	}
	
	
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof Map){
			Map temp = (Map)value;
	        for (Object key : temp.keySet()) {
	        	if("accid".equals(key)){
	        		Object tempt = temp.get(key);
	        		this.accid = tempt+"";
	        	}
	        	if("isVirtual".equals(key)){
	        		Object tempt = temp.get(key);
	        		this.virtual = (Boolean)tempt;
	        		if(this.virtual){
	        			this.isBtnState = 0;
	        		}else{
	        			this.isBtnState = 1;
	        		}
	        		registerBean("isBtnState", this.isBtnState);
	        	}
	        	if("isSelf".equals(key)){
	        		boolean self = (Boolean) temp.get(key);
	        		this.isSelf = self;
	        	}
	        }
	        registerBean("virtual", this.virtual);
	        registerBean("accid", this.accid);
	        registerBean("isSelf", this.isSelf);
		}
	}
	
	@Command
	String handleRefresh(InputEvent event) {
		if("TopRefresh".equals(event.getEventType())) {
			if (log.isDebugEnabled()) {
				log.debug("sellTradingAccount : getTradingAccountInfo");
			}
			if(accid!=null)
			tradingService.getSyncTradingAccountInfo(accid);
		}
		return null;
	}
	
	//卖出
	@Command(navigations={@Navigation(on="SellStockPage",showPage="SellStockPage"),
			@Navigation(on = "GeGuStockPage", showPage = "GeGuStockPage") })
	CommandResult sellStockItemClick(InputEvent event){
		String stockCode = null; //股票代码
		String stockName = null; //股票名称
		String stockMarketCode = null; //市场代码
		Long buyPrice = 0l; //委托价格
		Long amount = 0L; //委托数量
		String status = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
			if (event.getProperty("position") instanceof Integer) {
				int position = (Integer) event.getProperty("position");
				CommandResult result = new CommandResult();
				if(tradingAccount!=null){
					List<StockTradingOrderBean> stockOrder = tradingAccount.getTradingOrders();
					if(stockOrder!=null && stockOrder.size()>0){
						StockTradingOrderBean stockTrading = stockOrder.get(position);
						status = stockTrading.getStatus();
						stockCode = stockTrading.getStockCode(); 
						stockName = stockTrading.getStockName(); 
						stockMarketCode = stockTrading.getMarketCode(); 
						amount = stockTrading.getAmount();
						buyPrice = stockTrading.getBuy();
						//交易盘id
						if(accid!=null){
							map.put("accid", accid);
						}
						if(stockName!=null){
							map.put("name", stockName);
						}
						if(stockCode!=null){
							map.put("code", stockCode);
						}
						if(stockMarketCode!=null){
							map.put("market", stockMarketCode);
						}
						if(amount!=null){
							map.put("amount", amount);
						}
						map.put(Constants.KEY_VIRTUAL_FLAG, virtual);
						for(int i=0; i<stockOrder.size();i++){
							StockTradingOrderBean temp = stockOrder.get(i);
							if(stockCode!=null && stockCode.equals(temp.getStockCode()) && temp.getStatus()==null){
								map.put("position", i);
							}
						}
					}
					StockSelection selection = new StockSelection(stockMarketCode, stockCode, stockName, buyPrice);
					if (isSelf) {
						selection.setType(1);
						result.setResult("SellStockPage");
					} else {
						selection.setType(0);
						result.setResult("GeGuStockPage");
					}
					updateSelection(selection);
				}
				if(map!=null && map.size()>0){
					result.setPayload(map);
				}
				
				return result;
			}
		}
		return null;
	}
	
	
	@Command(navigations={@Navigation(on="SellStockPage",showPage="SellStockPage")})
	CommandResult sellTradingAction(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			HashMap<String, Object> map = new HashMap<String, Object>();
			CommandResult result = new CommandResult();
			if(tradingAccount!=null){
				List<StockTradingOrderBean> stockOrder = tradingAccount.getTradingOrders();
				if(stockOrder!=null && stockOrder.size()>0){
					StockTradingOrderBean order = stockOrder.get(0);
					if(order!=null){
						Long buyPrice = order.getBuy();
						String code = order.getStockCode();
						String name = order.getStockName();
						String market = order.getMarketCode();
						Long amount = order.getAmount();
						map.put(Constants.KEY_NAME_FLAG, name);
						map.put(Constants.KEY_CODE_FLAG, code);
						map.put(Constants.KEY_MARKET_FLAG, market);
						map.put(Constants.KEY_VIRTUAL_FLAG, virtual);
						map.put("amount", amount);
						updateSelection(new StockSelection(market, code, name, buyPrice));
					}
				}
			}
			if(accid!=null){
				map.put("accid", accid);
			}
			map.put("position", 0);
			result.setPayload(map);
			result.setResult("SellStockPage");
			return result;
		}
		return null;
	}
	
	@Bean
	boolean enabled_view;
	
	
	
	
	
	/**
	 * 清算交易盘
	 * @param acctID - 交易盘Id
	 */	
	@Command(navigations = { @Navigation(on = "*", message = "是否确定清仓？", params = {
			@Parameter(name = "title", value = "提示"),
			@Parameter(name = "icon", value = "resourceId:drawable/remind_focus"),
			@Parameter(name = "onOK", value = "leftok"),
			@Parameter(name = "onCanceled", value = "取消") }) })
	String handlerClearClick(InputEvent event) {
		return "";
	}
	
	@Command(uiItems=@UIItem(id="leftok",label="确定",icon="resourceId:drawable/home"))
	String clearTradingAccount(InputEvent event){
		enabled_view = false;
	    registerBean("enabled_view", enabled_view);
		IView v = (IView)event.getProperty(InputEvent.PROPERTY_SOURCE_VIEW);
		if(v != null)
			v.hide();
		if(tradingService!=null && accid!=null){
			try{
				tradingService.clearTradingAccount(accid);
			}catch(Exception e){
				enabled_view = true;
			    registerBean("enabled_view", enabled_view);
			}
			
		}
		return null;
	}	
}
