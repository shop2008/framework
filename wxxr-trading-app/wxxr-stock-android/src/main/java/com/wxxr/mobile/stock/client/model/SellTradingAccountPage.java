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
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.mobile.stock.client.utils.Utils;

@View(name="sellTradingAccount",withToolbar=true, description="实盘/模拟",provideSelection=true)
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY,layoutId="R.layout.sell_trading_account_info_page_layout")
public abstract class SellTradingAccountPage extends PageBase implements IModelUpdater {

	static Trace log = Trace.getLogger(SellTradingAccountPage.class);
	
	@Menu(items={"left","right"})
	private IMenu toolbar;
	
 
	@Command(description="Invoke when a toolbar item was clicked",uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
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
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="multiple",value="100")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple",value="100")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor1;
	
	@Convertor(params={
			@Parameter(name="format",value="M月d日买入"),
			@Parameter(name="nullString",value="--")
	})
	LongTime2StringConvertor longTime2StringConvertorBuy;
	
	@Convertor(params={
			@Parameter(name="format",value="M月d日卖出"),
			@Parameter(name="nullString",value="--")
	})
	LongTime2StringConvertor longTime2StringConvertorSell;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorYuan;	

	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;	
	/** 交易盘编号*/
	private long id;
	
	/**买入日期  */
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.buyDay:null}", converter = "longTime2StringConvertorBuy")
	String buyDay;  
	
	/**卖出日期 */
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.sellDay:null}", converter = "longTime2StringConvertorSell")
	String sellDay;  
	
	/**申购金额*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.applyFee:'--'}",converter="stockLong2StringAutoUnitConvertor")
	String applyFee;
	
	/**可用资金*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.avalibleFee:'--'}",converter="stockLong2StringAutoUnitConvertor1")
	String avalibleFee;
	
	/**总盈亏率*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.gainRate:'--'}",converter="stockLong2StringConvertorSpecial",attributes={
			@Attribute(name = "textColor", value = "${(tradingAccount!=null && tradingAccount.gainRate>0)?'resourceId:color/red':((tradingAccount!=null && tradingAccount.gainRate<0)?'resourceId:color/green':'resourceId:color/white')}")
			})
	String gainRate;  
	
	/**总盈亏额*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.totalGain:'--'}", converter = "stockLong2StringConvertorYuan" ,attributes={
			@Attribute(name = "textColor", value = "${(tradingAccount!=null && tradingAccount.totalGain>0)?'resourceId:color/red':((tradingAccount!=null && tradingAccount.totalGain<0)?'resourceId:color/green':'resourceId:color/white')}")
			})
	String totalGain;
	
	/**交易订单列表*/
	@Field(valueKey="options",binding="${tradingAccount!=null?tradingAccount.tradingOrders:null}")
	List<StockTradingOrderBean> stockTradingOrder;
	/**交易订单
	 * 为空：按钮不可用
	 * 非空：按钮可用
	 * */
	@Field(valueKey="enabled",enableWhen="${(tradingAccount.tradingOrders!=null&&tradingAccount.tradingOrders.size()>0)}")
	boolean isRedSellBtn;
	
	@Field(valueKey="enabled",enableWhen="${(tradingAccount.tradingOrders!=null&&tradingAccount.tradingOrders.size()>0)}")
	boolean isRedCleanBtn;
	
	@Field(valueKey="enabled",enableWhen="${(tradingAccount.tradingOrders!=null&&tradingAccount.tradingOrders.size()>0)}")
	boolean isBlueSellBtn;
	
	@Field(valueKey="enabled",enableWhen="${(tradingAccount.tradingOrders!=null&&tradingAccount.tradingOrders.size()>0)}")
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
	
	
	@Field(attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
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
	
	@Bean
	Utils utils = Utils.getInstance();
	@OnShow
	void initData(){
		registerBean("virtual", true);
		registerBean("accid", accid);
		if(getAppToolbar()!=null){
			if(this.virtual){
				getAppToolbar().setTitle("模拟盘", null);
			}else{
				getAppToolbar().setTitle("实盘", null);
			}
		}
	}
	
	/**
	 * 订单详情点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "right", label = "交易详情", icon = "resourceId:drawable/jyjl") }, 
			navigations = { @Navigation(on = "*", showPage = "TradingRecordsPage")
			})
	CommandResult toolbarClickedRight(InputEvent event) {
		CommandResult resutl = new CommandResult();
		Long stockId = 0L;
		if (event.getProperty("position") instanceof Integer) {
			int position = (Integer) event.getProperty("position");
			if (stockTradingOrder != null && stockTradingOrder.size() > 0) {
				StockTradingOrderBean tempTradingA = stockTradingOrder.get(position);
				stockId = tempTradingA.getId();
			}
		}
		resutl.setResult("TradingRecordsPage");
		resutl.setPayload(stockId);
		return resutl;
	}
	
	
	@Command(navigations = { 
			@Navigation(on = "TBuyStockInfoPage", showPage = "TBuyStockInfoPage"),
			@Navigation(on = "ShiPanBuyStockInfoPage", showPage = "ShiPanBuyStockInfoPage") 
			})
	CommandResult handleStockClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			if (tradingAccount != null) {
				resutl.setPayload(tradingAccount.getId());
				if(tradingAccount.getVirtual()){
					resutl.setResult("TBuyStockInfoPage");
				}else{
					resutl.setResult("ShiPanBuyStockInfoPage");
				}
				return resutl;
			}
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
	String handleTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("sellTradingAccount : handleTMegaTopRefresh");
		}
		if(accid!=null)
		tradingService.getTradingAccountInfo(accid);
		return null;
	}
	
	//卖出
	@Command(navigations={@Navigation(on="SellStockPage",showPage="SellStockPage")})
	CommandResult sellStockItemClick(InputEvent event){
		String stockCode = null; //订单id
		Long id = null; //股票代码
		String stockName = null; //股票名称
		String stockMarketCode = null; //市场代码
		Long amount = 0L;
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())){
			if (event.getProperty("position") instanceof Integer) {
				int position = (Integer) event.getProperty("position");
				if(tradingAccount!=null){
					List<StockTradingOrderBean> stockOrder = tradingAccount.getTradingOrders();
					if(stockOrder!=null && stockOrder.size()>0){
						StockTradingOrderBean stockTrading = stockOrder.get(position);
						id= stockTrading.getId(); 
						stockCode = stockTrading.getStockCode(); 
						stockName = stockTrading.getStockName(); 
						stockMarketCode = stockTrading.getMarketCode(); 
						amount = stockTrading.getAmount();
						//交易盘id
						if(id!=null){
							map.put("orderId", id);
						}
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
						map.put("position", position);
					}
				}
				CommandResult result = new CommandResult();
				if(map!=null && map.size()>0){
					result.setPayload(map);
					updateSelection(map);
				}
				result.setResult("SellStockPage");
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
						String code = order.getStockCode();
						String name = order.getStockName();
						Long amount = order.getAmount();
						map.put("stockName", name);
						map.put("stockCode", code);
						map.put("amount", amount);
					}
				}
			}
			if(accid!=null)
				map.put("accid", accid);
			map.put("position", 0);
			result.setPayload(map);
			result.setResult("SellStockPage");
			return result;
		}
		return null;
	}
	/**
	 * 清算交易盘
	 * @param acctID - 交易盘Id
	 */
	@Command
	String clearTradingAccount(InputEvent event){
		if(tradingService!=null && accid!=null){
			tradingService.clearTradingAccount(accid);
		}
		return null;
	}
}
