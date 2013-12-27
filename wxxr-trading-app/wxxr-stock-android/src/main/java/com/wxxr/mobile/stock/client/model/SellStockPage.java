package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
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
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.mobile.stock.client.utils.Utils;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;


/**
 * 卖出
 * BuyStockDetailInputView需要只需要昨收价和拥有的最大股数
 * StockInputKeyboard需要昨收价和最大股数
 * 
 * @author xijiadeng
 * 
 */
@View(name="SellStockPage",withToolbar=true,description="卖出",provideSelection=true)
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.sell_stock_page_layout")
public abstract class SellStockPage extends PageBase implements IModelUpdater {

	static Trace log = Trace.getLogger(SellStockPage.class);
	@Menu(items={"left"})
	private IMenu toolbar; 
	
	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
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
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getTradingAccountInfo(accid)}")
	TradingAccountBean tradingAccount;
	
	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getStockQuotation(stockCode,stockMarket)}")
	StockQuotationBean stockQuotation;

	@Convertor(params={
			@Parameter(name="multiple",value="1000"),
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor;	
	
	@ViewGroup(viewIds={"StockQuotationView","SellFiveDayMinuteLineView","StockKLineView"})
	private IViewGroup contents;	
	
	@Bean
	int size;
	@Bean
	int indexGroupPosition;
	
	@Field(valueKey = "text", attributes = {
			@Attribute(name = "size", value = "${size}"),
			@Attribute(name = "position", value = "${indexGroupPosition}") })
	String indexGroup;
	
	@Bean
	String stockCode; //股票代码
	@Bean
	Long orderId ; //订单id
	@Bean
	String stockName; //股票名称
	@Bean
	String stockMarket; //市场代码
	
	@Bean
	String accid; //交易盘ID
	
	@Bean
	int isSelected = 0; //1为市价点中，卖出价格为0即为市价卖出
	
	@Field(attributes={
			@Attribute(name = "enabled", value = "${isSelected==0}")
	})
	String priceEdit;
	
	@Field(attributes={
			@Attribute(name = "checked", value = "${isSelected==0}")
	})
	String isChecked;
	
	@Field(attributes={
			@Attribute(name = "checked", value = "${isSelected==1}")
	})
	String isChecked1;
	
	/**交易订单列表*/
	@Field(valueKey="options",binding="${TradingOrder!=null?TradingOrder:null}",attributes={
			@Attribute(name = "position", value = "${position}")
	})
	List<StockTradingOrderBean> stockTradingOrder;
	
	@Bean
	List<StockTradingOrderBean> TradingOrder;
	
	@Bean
	List<String> sellStockData;
	
	@Field(valueKey="text",binding="${stockQuotation!=null?stockQuotation.newprice:0}",converter="stockLong2StringAutoUnitConvertor")
	String newprice;
	DataField<String> newpriceField;
	
	@Bean
	String sellPrice; //卖出价格，卖出点击使用
	
	@Bean
	String amount; //卖出数量;
	
	@Field(valueKey="enabled",enableWhen="${amount!=null}")
	boolean isSellBuyBtn = false;
	
	@Bean
	int position=0;
	
	@Bean
	String defStockNameCode;
	
	@Bean
	String maxAmountBean;
	//输入键盘
	@Field(valueKey = "text", attributes = {
			@Attribute(name = "type", value = "1"),
			@Attribute(name = "marketPrice", value = "${closePriceBean}"),
			@Attribute(name = "sellCount", value = "${maxAmountBean}") })
	String inputView;
	
	@Bean
	String closePriceBean; //作收价格，传入keyboard计算%
	
	@Field(valueKey = "visible")
	boolean progress;

	@Field(valueKey = "visible")
	boolean refresh = true;
	
	@Command
	String handlerPageChanged(InputEvent event) {
		Object p = event.getProperty("position");
		Object s = event.getProperty("size");
		if(p  instanceof Integer) {
			this.indexGroupPosition = (Integer)p;
			this.size = (Integer)s;
		}
		registerBean("size", size);
		registerBean("indexGroupPosition", indexGroupPosition);
		log.debug("GeGuStockPage handlerPageChanged position: " + indexGroupPosition + "size: "+size);
		return null;
	}
	
	@Command()
	String handlerRefreshClicked(InputEvent event) {
		if(stockQuotation!=null) {
			long price = stockQuotation.getNewprice();
			String val = String.format("%.2f", price/1000f);
			newpriceField.setValue("");
			newpriceField.setValue(val);
		}
		infoCenterService.getSyncStockQuotation(stockCode, stockMarket);
		
		// 需要回调
		StockQuotationBean stockQuotation = infoCenterService.getSyncStockQuotation(stockCode, stockMarket);
		String sellPrice = stockQuotation.getNewprice() + "";
		closePriceBean = stockQuotation.getClose() + "";
		registerBean("closePriceBean", closePriceBean);
		registerBean("sellPrice", sellPrice);
		//刷新viewpager
		updateSelection(new StockSelection(stockMarket, stockCode, stockName,1));
		return null;
	}
	
	@Command
	String dropDownMenuListItemClick(InputEvent event){
		if("SpinnerItemSelected".equals(event.getEventType())){
			if (event.getProperty("position") instanceof Integer) {
				int position = (Integer) event.getProperty("position");
				StockTradingOrderBean stockTrading = tradingAccount.getTradingOrders().get(position);
				long buyPrice = 0;
				if(stockTrading!=null){
					maxAmountBean = stockTrading.getAmount() + "";
					this.stockMarket = stockTrading.getMarketCode();
					this.stockCode = stockTrading.getStockCode();
					buyPrice = stockTrading.getBuy();
				}
				if(stockInfoSyncService!=null){
					StockBaseInfo stockInfo = this.stockInfoSyncService.getStockBaseInfoByCode(this.stockCode, this.stockMarket);
					this.stockName = stockInfo.getName();
				}
				registerBean("stockCode", this.stockCode);
				registerBean("stockName", this.stockName);
				registerBean("stockMarket", this.stockMarket);
				registerBean("maxAmountBean", this.maxAmountBean);
				log.info("SellStockPage SpinnerItemSelected: stockCode = "+this.stockCode +"stockMarket = "+this.stockMarket);
				this.stockQuotation = this.infoCenterService.getStockQuotation(stockCode, stockMarket);
				registerBean("stockQuotation", this.stockQuotation);
				//刷新viewpager
				updateSelection(new StockSelection(stockMarket, stockCode, stockName,buyPrice, 1));
			}
		}
		return null;
	}
	
	@OnShow
	protected void initData(){
		registerBean("isChecked", isSelected);
		if(tradingAccount!=null){
			List<StockTradingOrderBean> stockTradingOrder = tradingAccount.getTradingOrders();
			if(stockTradingOrder!=null && stockTradingOrder.size()>0){
				List<StockTradingOrderBean> tempOrder = new ArrayList<StockTradingOrderBean>();
				for(int i=0; i<stockTradingOrder.size();i++){
					StockTradingOrderBean tempStock = stockTradingOrder.get(i);
					if(tempStock!=null){
						String status = tempStock.getStatus();
						if(status==null){
							StockTradingOrderBean temp = tempStock;
							tempOrder.add(temp);
						}
					}
				}
				if(tempOrder!=null && tempOrder.size()>0){
					this.TradingOrder = tempOrder;
					registerBean("TradingOrder", this.TradingOrder);
				}
			}
		}
	}
	
	//挂单
	@Command
	String GuaDanOnClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			this.isSelected = 0;
			registerBean("isChecked", this.isSelected);
		}
		return null;
	}
	//市价
	@Command
	String ShiJiaOnClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			this.isSelected = 1;
			registerBean("isChecked", this.isSelected);
		}
		return null;
	}
	
	
	@Command
	String priceTextChanged(InputEvent event) {
		String key = (String) event.getProperty("changedText");
		String value = "0";
		try {
			if(!StringUtils.isEmpty(key)){
				value = (long) Utils.roundUp(Float.parseFloat(key) * 1000, 0) + "";
				sellPrice = value;
			}else{
				sellPrice = key;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		closePriceBean = stockQuotation.getClose()+ "";
		registerBean("closePriceBean", closePriceBean);
		return null;
	}
	
	@Command
	String countTextChanged(InputEvent event) {
		String key = (String) event.getProperty("changedText");
		amount = key;
		registerBean("amount", key);
		return null;
	}
	
	@Override
	public void updateModel(Object data) {
		registerBean("size", 3);
		registerBean("indexGroupPosition", 0);
		HashMap<String, String> tempMap = new HashMap<String, String>();
		if(data instanceof Map){
			Map temp = (Map) data;
			if(temp!=null && temp.size()>0){
				 for (Object key : temp.keySet()) {
		        	if("accid".equals(key)){
		        		if(temp.get(key) instanceof String){
		        			this.accid = (String) temp.get(key);
		        			registerBean("accid", this.accid);
		        		}
		        	}
		        	if("orderId".equals(key)){
		        		if(temp.get(key) instanceof String){
		        			this.orderId = (Long) temp.get(key);
		        			registerBean("orderId", this.orderId);
		        		}
		        	}
		        	if("name".equals(key)){
		        		if(temp.get(key) instanceof String){
		        			this.stockName = (String) temp.get(key);
		        			registerBean("stockName", this.stockName);
		        		}
		        	}
		        	if("code".equals(key)){
		        		if(temp.get(key) instanceof String){
		        			this.stockCode = (String) temp.get(key);
		        			registerBean("stockCode", this.stockCode);
		        		}
		        	}
		        	if("market".equals(key)){
		        		if(temp.get(key) instanceof String){
		        			this.stockMarket = (String) temp.get(key);
		        			registerBean("stockMarket", this.stockMarket);
		        		}
		        	}
		        	if("amount".equals(key)){
		        		if(temp.get(key) instanceof Long){
		        			this.maxAmountBean = temp.get(key) + "";
		        			registerBean("maxAmountBean", this.maxAmountBean);
		        		}
		        	}
		        	if("position".equals(key)){
		        		if(temp.get(key) instanceof Integer){
		        			this.position = (Integer) temp.get(key);
		        			registerBean("position", this.position);
		        		}
		        	}
		        }
				 if(this.stockName!=null && this.stockCode!=null){
					 this.defStockNameCode = this.stockName+""+this.stockCode;
					 registerBean("defStockNameCode", defStockNameCode);
				 }
			}
		}
	}
	
	@Command(commandName="sellStockClick",navigations = { 
			@Navigation(on = "StockAppBizException", message = "%m", params = {
					@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2")})				
			}
	)
	@ExeGuard(title="提示", message="正在获取数据，请稍后...", silentPeriod=1, cancellable=false)
	String sellStockClick(InputEvent event){
		String price = null;
		if(isSelected == 0) {
			try {
				if(sellPrice!=null && !StringUtils.isEmpty(sellPrice)){
					price = Long.parseLong(sellPrice)/10 + "";
				}
				tradingService.sellStock(accid, stockMarket, stockCode, price, amount);
			}catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}else{
			tradingService.sellStock(accid, stockMarket, stockCode, "0", amount);
		}
		IView v = (IView)event.getProperty(InputEvent.PROPERTY_SOURCE_VIEW);
		v.hide();
		return "";
	}
	
	@OnUIDestroy
	void destroyData() {
		isSelected = 0;
		stockMarket = null;
		stockCode = null;
		amount = null;
		accid = null;
		sellPrice = null;
		closePriceBean = null;
		registerBean("stockMarket", "");
		registerBean("stockCode", "");
		registerBean("amount", "");
		registerBean("accid", "");
		registerBean("sellPrice", "");
		registerBean("closePriceBean", "");
	}
}
