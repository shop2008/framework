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
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.LineListBean;
import com.wxxr.mobile.stock.app.bean.StockLineBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteLineBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;



@View(name="SellStockPage",withToolbar=true,description="卖出")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.sell_stock_page_layout")
public abstract class SellStockPage extends PageBase implements IModelUpdater {

	static Trace log = Trace.getLogger(SellStockPage.class);
	@Menu(items={"left"})
	private IMenu toolbar;
	
	@ViewGroup(viewIds={"readRecord","auditDetail","mnAuditDetail"},defaultViewId="readRecord")
	private IViewGroup contents;

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
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getStockQuotation(stockCode,stockMarketCode)}")
	StockQuotationBean stockQuotation;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getDayline(stockCode,stockMarketCode)}")
	LineListBean lineList;
	
	@Field(valueKey="options",binding="${lineList!=null?lineList.day_list:null}")
	List<StockLineBean> dayList;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getMinuteline(map)}")
	StockMinuteKBean minute;
	
	@Field(valueKey="options",binding="${minute!=null?minute.list:null}",attributes={
			@Attribute(name = "stockClose", value = "${minute!=null?minute.close:'10060'}"),
			@Attribute(name = "stockDate", value = "${minute!=null?minute.date:'20131125'}"),
			@Attribute(name = "stockBorderColor",value="#ACACAC"),
			@Attribute(name = "stockUpColor",value="#BA2514"),
			@Attribute(name = "stockDownColor",value="#3C7F00"),
			@Attribute(name = "stockAverageLineColor",value="#FFE400"),
			@Attribute(name = "stockCloseColor",value="#FFFFFF")
	})
	List<StockMinuteLineBean> minute1;
	
	@Bean
	Map<String, String> map;
	
	@Bean
	String stockCode; //股票代码
	@Bean
	Long orderId ; //订单id
	@Bean
	String stockName; //股票名称
	@Bean
	String stockMarketCode; //市场代码
	
	@Bean
	String accid; //交易盘ID
	
	@Bean
	int isSelected = 0;
	
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
	
	@Field(valueKey="options",binding="${sellStockData!=null?sellStockData:null}",attributes={
			@Attribute(name = "text", value = "${defStockNameCode!=null?defStockNameCode:'--'}")
			})
	List<String> sellStockOrder;
	
	@Bean
	List<String> sellStockData;
	
	@Bean
	String defStockNameCode = "--";
	
	// 股票或指数 代码+市场代码
	@Field(valueKey="text",binding="${stockQuotation!=null?stockQuotation.code:'--'}${'.'}${stockQuotation!=null?stockQuotation.market:'--'}")
	String codeMarket;
	
	//股票名称
	@Field(valueKey="text",binding="${stockName!=null?stockName:'--'}")
	String name;
	
	// 换手率
	@Field(valueKey="text",binding="${stockQuotation!=null?stockQuotation.handrate:'--'}")
	String handrate;
	
	// 量比
	@Field(valueKey="text",binding="${stockQuotation!=null?stockQuotation.lb:'--'}")
	String lb;
	
	//成交额
	@Field(valueKey="text",binding="${stockQuotation!=null?stockQuotation.secuamount:'--'}")
	String secuamount;
	
	// 流通盘
	@Field(valueKey="text",binding="${stockQuotation!=null?stockQuotation.capital:'--'}")
	String capital;
	
	
	
	@Command()
	String retryLoadingClick(InputEvent event){
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
						StockName.add(name+""+code);
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
	String stockChanged(InputEvent event){
		if(InputEvent.EVENT_TYPE_TEXT_CHANGED.equals(event.getEventType())){
			
		}
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
		        	if("stockName".equals(key)){
		        		if(temp.get(key) instanceof String){
		        			this.stockName = (String) temp.get(key);
		        			registerBean("stockName", this.stockName);
		        		}
		        	}
		        	if("stockCode".equals(key)){
		        		if(temp.get(key) instanceof String){
		        			this.stockCode = (String) temp.get(key);
		        			registerBean("stockCode", this.stockCode);
		        		}
		        	}
		        	if("stockMarketCode".equals(key)){
		        		if(temp.get(key) instanceof String){
		        			this.stockMarketCode = (String) temp.get(key);
		        			registerBean("stockMarketCode", this.stockMarketCode);
		        		}
		        	}
		        }
				 if(this.stockName!=null && this.stockCode!=null){
					 this.defStockNameCode = this.stockName+""+this.stockCode;
					 registerBean("defStockNameCode", defStockNameCode);
				 }
				 if(this.stockCode!=null && this.stockMarketCode!=null){
					 tempMap.put(this.stockCode, this.stockMarketCode);
					 this.map = tempMap;
					 registerBean("map", tempMap);
				 }
			}
		}
	}
}
