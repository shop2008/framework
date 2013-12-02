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
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteLineBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="GeGuStockPage",withToolbar=true,description="个股界面")
@AndroidBinding(type=AndroidBindingType.ACTIVITY, layoutId="R.layout.gegu_page_layout")
public abstract class GeGuStockPage extends PageBase implements IModelUpdater {
	
	static Trace log = Trace.getLogger(GeGuStockPage.class);
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getStockQuotation(codeValue,marketCode)}")
	StockQuotationBean quotationBean;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getMinuteline(map)}")
	StockMinuteKBean minute;
	
	@Convertor(params={
			@Parameter(name="multiple",value="100.00"),
			@Parameter(name="format",value="%.2f")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	
	@Field(attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String acctRefreshView;
	
	@Command
	String handleTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("GeGuStockPage : handleTMegaTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		infoCenterService.getStockQuotation(codeValue,marketCode);
		infoCenterService.getMinuteline(map);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}	
	
	@Bean
	Map<String, String> map;
	
	@Bean
	String codeValue; //股票代码
	 
	@Bean
	String marketCode; // 市场代码
	
	@Bean
	String stockName;
	
	@Field(valueKey="text",binding="${stockName!=null?stockName:'--'}")
	String name;
	
	@Field(valueKey="text",binding="${(quotationBean!=null && quotationBean.code!=null)?quotationBean.code:'--'}${'.'}${(quotationBean!=null && quotationBean.market!=null)?quotationBean.market:'--'}")
	String codeAndmarket;
	
	@Field(valueKey="text",binding="${(quotationBean!=null && quotationBean.risefallrate!=null)?quotationBean.risefallrate:'--'}")
	String risefallrate;
	
	@Field(valueKey="text",binding="${(quotationBean!=null && quotationBean.change!=null)?quotationBean.change:'--'}",converter="stockLong2StringAutoUnitConvertor")
	String change;
	
	@Field(valueKey="text",binding="${(quotationBean!=null && quotationBean.newprice!=null)?quotationBean.newprice:'--'}",converter="stockLong2StringAutoUnitConvertor")
	String newprice;
	
	@Field(valueKey="text",binding="${(quotationBean!=null && quotationBean.datetime!=null)?quotationBean.datetime:'--'}")
	String datetime;
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.close:'--'}",converter="stockLong2StringAutoUnitConvertor")
	String close;// 昨收
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.open:'--'}",converter="stockLong2StringAutoUnitConvertor" )
	String open;// 开盘
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.high:'--'}",converter="stockLong2StringAutoUnitConvertor")
	String high;// 最高
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.low:'--'}",converter="stockLong2StringAutoUnitConvertor")
	String low;// 最低
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.averageprice:'--'}",converter="stockLong2StringAutoUnitConvertor")
	String averageprice;// 均价
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.profitrate:'--'}")
	String profitrate;// 市盈率
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.lb:'--'}",converter="stockLong2StringAutoUnitConvertor")
	String lb;// 量比
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.handrate:'--'}")
	String handrate;// 换手率
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.secuvolume:'--'}")
	String secuvolume;// 成交量
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.secuamount:'--'}")
	String secuamount;// 成交额
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.capital:'--'}")
	String capital;// 流通盘
	
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.marketvalue:'--'}")
	String marketvalue;// 市值
	
	@Field(valueKey="options",binding="${minute!=null?minute.list:null}",attributes={
			@Attribute(name = "stockClose", value = "${minute!=null?minute.close:'0'}"),
			@Attribute(name = "stockDate", value = "${minute!=null?minute.date:'0'}"),
			@Attribute(name = "stockBorderColor",value="#535353"),
			@Attribute(name = "stockUpColor",value="#BA2514"),
			@Attribute(name = "stockDownColor",value="#3C7F00"),
			@Attribute(name = "stockAverageLineColor",value="#FFE400"),
			@Attribute(name = "stockCloseColor",value="#FFFFFF")
	})
	List<StockMinuteLineBean> stockMinuteData;
	
	@OnShow
	protected void initData(){
		
	}
	
	//挑战交易盘买入
	@Command(navigations={
			@Navigation(on = "tiaozhan",showPage="QuickBuyStockPage")
	})
	CommandResult tiaoZhanTradingBuyClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = payData();
			temp.put("tiaozhan", "tiaozhan");
			result.setPayload(temp);
			result.setResult("tiaozhan");
			return result;
		}
		return null;
	}
	//参数交易盘买入
	@Command(navigations={
			@Navigation(on = "cansai",showPage="QuickBuyStockPage")
	})
	CommandResult canSaiTradingBuyClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = payData();
			temp.put("cansai", "cansai");
			result.setPayload(temp);
			result.setResult("cansai");
			return result;
		}
		return null;
	}
	
	private HashMap<String, Object> payData(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		if(quotationBean!=null){
			tempMap.put("code", quotationBean.getCode());
			tempMap.put("market", quotationBean.getMarket());
		}
		return tempMap;
	}
	
	@Override
	public void updateModel(Object value) {
		HashMap<String, String> tempMap = new HashMap<String, String>();
		if(value instanceof Map){
			Map data = (Map) value;
			for (Object key : data.keySet()) {
				if(key.equals("code")){
					String code = (String)data.get(key);
					this.codeValue = code;
					registerBean("codeValue", this.codeValue);
				}
				if(key.equals("market")){
					String market = (String)data.get(key);
					this.marketCode = market;
					registerBean("marketCode", this.marketCode);
				}
				if(key.equals("name")){
					String name = (String) data.get(key);
					this.stockName = name;
					registerBean("stockName", this.stockName);
				}
				if(this.codeValue!=null && this.marketCode!=null){
					tempMap.put(this.codeValue, this.marketCode);
					registerBean("map", tempMap);
				}
	        }
		}
	}
}
