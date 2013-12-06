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
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.mobile.stock.client.utils.Utils;


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
	IInfoCenterManagementService infoCenterService;
	
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
	
	@ViewGroup(viewIds={"StockQuotationView","GZMinuteLineView", "StockKLineView"})
	private IViewGroup contents;	
	
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
	@Field(valueKey="options",binding="${tradingAccount!=null?tradingAccount.tradingOrders:null}")
	List<StockTradingOrderBean> stockTradingOrder;
	
	@Bean
	List<String> sellStockData;
	
	@Field(valueKey="text",binding="${stockQuotation!=null?stockQuotation.newprice:null}",converter="stockLong2StringAutoUnitConvertor")
	String newprice;
	
	@Bean
	String sellPrice; //卖出价格，卖出点击使用
	
	@Bean
	String amount; //卖出数量;
	
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
	
	@Command()
	String retryLoadingClick(InputEvent event) {
		// 需要回调
		infoCenterService.getStockQuotation(stockCode, stockMarket);
		sellPrice = stockQuotation.getNewprice() + "";
		closePriceBean = stockQuotation.getClose() + "";
		registerBean("closePriceBean", closePriceBean);
		registerBean("sellPrice", sellPrice);
		//刷新viewpager
		String[] stockInfos = new String[] { stockCode, stockName, stockMarket };
		updateSelection((Object) stockInfos);
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("code", stockCode);
		map.put("name", stockName);
		map.put("market", stockMarket);
		updateSelection(map);
		return null;
	}
	
	
	@Command
	String dropDownMenuListItemClick(InputEvent event){
		if("SpinnerItemSelected".equals(event.getEventType())){
			if (event.getProperty("position") instanceof Integer) {
				int position = (Integer) event.getProperty("position");
				StockTradingOrderBean stockTrading = tradingAccount.getTradingOrders().get(position);
				maxAmountBean = stockTrading.getAmount() + "";
				this.stockCode = stockTrading.getStockCode();
				this.stockName = stockTrading.getStockName();
				this.stockMarket = stockTrading.getMarketCode();
				registerBean("stockCode", this.stockCode);
				registerBean("stockName", this.stockName);
				registerBean("stockMarket", this.stockMarket);
				registerBean("maxAmountBean", this.maxAmountBean);
				//刷新viewpager
				String[] stockInfos = new String[] { stockCode, stockName, stockMarket };
				updateSelection((Object) stockInfos);
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("code", stockCode);
				map.put("name", stockName);
				map.put("market", stockMarket);
				updateSelection(map);
			}
		}
		return null;
	}
	/**
	 * 卖出股票
	 * @param acctID -交易盘ID
	 * @param market -市场代码： SH，SZ各代表上海，深圳
	 * @param code -股票代码
	 * @param price -委托价
	 * @param amount -委托数量
	 * @throws StockAppBizException
	 */
	@Command
	String sellStock(InputEvent event){
		String price = "0";
		if(isSelected == 0) {
			try {
				price = Long.parseLong(sellPrice)/10 + "";
			}catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(tradingService!=null){
			if(accid!=null && stockMarket!=null && stockCode!=null && price!=null && amount!=null){
				tradingService.sellStock(accid, stockMarket, stockCode, price, amount);
			}
		}
		return null;
	}
	
	@OnShow
	protected void initData(){
		registerBean("isChecked", isSelected);
		ArrayList<String> StockName = new ArrayList<String>();
		if(tradingAccount!=null){
			List<StockTradingOrderBean> stockTradingOrder = tradingAccount.getTradingOrders();
			if(stockTradingOrder!=null && stockTradingOrder.size()>0){
				for(int i=0; i<stockTradingOrder.size();i++){
					StockTradingOrderBean tempStock = stockTradingOrder.get(i);
					if(tempStock!=null){
						String code = tempStock.getStockCode();
						String name = tempStock.getStockName();
						if(name==null){
							name = "--";
						}
						StockName.add(name+" "+code);
					}
				}
			}
		}
		if(StockName!=null && StockName.size()>0){
			registerBean("sellStockData", StockName);
		}
	}
	
	//挂单
	String GuaDanOnClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			this.isSelected = 0;
			registerBean("isChecked", this.isSelected);
		}
		return null;
	}
	//市价
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
			if(!StringUtils.isEmpty(key))
				value = (long) Utils.roundUp(Float.parseFloat(key) * 1000, 0) + "";
			sellPrice = value;
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
		        }
				 if(this.stockName!=null && this.stockCode!=null){
					 this.defStockNameCode = this.stockName+""+this.stockCode;
					 registerBean("defStockNameCode", defStockNameCode);
				 }
			}
		}
	}
}
