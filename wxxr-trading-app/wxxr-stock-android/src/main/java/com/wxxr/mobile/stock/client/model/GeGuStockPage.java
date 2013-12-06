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
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
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
	
	@Convertor(params={
			@Parameter(name="multiple",value="1000"),
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="1000"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;	
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="100"),
			@Parameter(name="formatUnit",value="手"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor convertorSecuvolume;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="1000"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor convertorSecuamount;
	
	@Field(attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String acctRefreshView;
	
	@Command
	String handleTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("GeGuStockPage : handleTMegaTopRefresh");
		}
		infoCenterService.getStockQuotation(codeValue,marketCode);
		infoCenterService.getMinuteline(map);
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
	
	@Bean
	List<String> counts;

	@ViewGroup(viewIds={"GZMinuteLineView", "StockKLineView"})
	private IViewGroup contents;	
	
	
	/**昨收*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.close:null}",converter="stockLong2StringAutoUnitConvertor")
	String close;
	
	/**开盘*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.open:null}",converter="stockLong2StringAutoUnitConvertor",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.open > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.open < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String open;
	
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.open > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.open < quotationBean.close)?'resourceId:color/green':'resourceId:color/tv_gray_color')}")
	})
	String openLabel;
	
	/**最高*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.high:null}",converter="stockLong2StringAutoUnitConvertor",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.high > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.high < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String high;
	
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.high > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.high < quotationBean.close)?'resourceId:color/green':'resourceId:color/tv_gray_color')}")
	})
	String highLabel;
	
	/**最底*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.low:null}",converter="stockLong2StringAutoUnitConvertor",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.low > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.low < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String low;
	
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.low > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.low < quotationBean.close)?'resourceId:color/green':'resourceId:color/tv_gray_color')}")	
	})
	String lowLabel;
	
	/**均价*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.averageprice:null}",converter="stockLong2StringAutoUnitConvertor",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.averageprice > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.averageprice < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String averageprice;
	
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.averageprice > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.averageprice < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String averagepriceLabel;
	
	/**市盈率*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.profitrate:null}",converter="stockLong2StringAutoUnitConvertor")
	String profitrate;
	
	/**量比*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.lb:null}",converter="stockLong2StringAutoUnitConvertor")
	String lb;
	
	/**换手率*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.handrate:null}",converter="stockLong2StringConvertorSpecial")
	String handrate; 
	
	/**成交量*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.secuvolume:null}",converter="convertorSecuvolume")
	String secuvolume;
	
	/**成交额*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.secuamount:null}",converter="convertorSecuamount")
	String secuamount;
	
	/**流通盘*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.capital:null}",converter="convertorSecuamount")
	String capital;
	
	/**市值*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.marketvalue:null}",converter="convertorSecuamount")
	String marketvalue;
	
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
			tempMap.put("name", this.stockName);
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
	        }
		}
	}
}
