package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
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
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUICreate;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="GeGuStockPage",withToolbar=true,description="个股界面",provideSelection=true)
@AndroidBinding(type=AndroidBindingType.ACTIVITY, layoutId="R.layout.gegu_page_layout")
public abstract class GeGuStockPage extends PageBase implements IModelUpdater, ISelectionChangedListener {
	
	static Trace log = Trace.getLogger(GeGuStockPage.class);
	@Menu(items = { "left" }) 
	private IMenu toolbar;

	private boolean hasShow = false;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
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
		this.infoCenterService.getStockQuotation(codeValue,marketCode);
		updateSelection(new StockSelection(quotationBean.getMarket(), quotationBean.getCode(), stockName));
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
	
	@Bean
	int size;
	@Bean
	int position;
	
	@Field(valueKey = "text", attributes = {
			@Attribute(name = "size", value = "${size}"),
			@Attribute(name = "position", value = "${position}") })
	String indexGroup;

	@ViewGroup(viewIds={"GZMinuteLineView", "StockKLineView"})
	private IViewGroup contents;	
	
	
	/**昨收*/
	@Field(valueKey="text",binding="${quotationBean!=null?quotationBean.close:null}",converter="stockLong2StringAutoUnitConvertor")
	String close;
	
	/**开盘*/
	@Field(valueKey="text",binding="${(quotationBean!=null && quotationBean.status==1) ?quotationBean.open:null}",converter="stockLong2StringAutoUnitConvertor",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.open > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.open < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String open;
	
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.open > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.open < quotationBean.close)?'resourceId:color/green':'resourceId:color/tv_gray_color')}")
	})
	String openLabel;
	
	/**最高*/
	@Field(valueKey="text",binding="${quotationBean!=null && quotationBean.status==1?quotationBean.high:null}",converter="stockLong2StringAutoUnitConvertor",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.high > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.high < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String high;
	
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.high > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.high < quotationBean.close)?'resourceId:color/green':'resourceId:color/tv_gray_color')}")
	})
	String highLabel;
	
	/**最底*/
	@Field(valueKey="text",binding="${quotationBean!=null && quotationBean.status==1?quotationBean.low:null}",converter="stockLong2StringAutoUnitConvertor",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.low > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.low < quotationBean.close)?'resourceId:color/green':'resourceId:color/white')}")
	})
	String low;
	
	@Field(valueKey="text",attributes={
			@Attribute(name = "textColor", value = "${(quotationBean!=null && quotationBean.low > quotationBean.close)?'resourceId:color/red':((quotationBean!=null && quotationBean.low < quotationBean.close)?'resourceId:color/green':'resourceId:color/tv_gray_color')}")	
	})
	String lowLabel;
	
	/**均价*/
	@Field(valueKey="text",binding="${quotationBean!=null && quotationBean.status==1?quotationBean.averageprice:null}",converter="stockLong2StringAutoUnitConvertor",attributes={
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
	@Field(valueKey="text",binding="${quotationBean!=null && quotationBean.status==1?quotationBean.lb:null}",converter="stockLong2StringAutoUnitConvertor")
	String lb;
	
	/**换手率*/
	@Field(valueKey="text",binding="${quotationBean!=null && quotationBean.status==1?quotationBean.handrate:null}",converter="stockLong2StringConvertorSpecial")
	String handrate; 
	
	/**成交量*/
	@Field(valueKey="text",binding="${quotationBean!=null && quotationBean.status==1?quotationBean.secuvolume:null}",converter="convertorSecuvolume")
	String secuvolume;
	
	/**成交额*/
	@Field(valueKey="text",binding="${quotationBean!=null && quotationBean.status==1?quotationBean.secuamount:null}",converter="convertorSecuamount")
	String secuamount;
	
	/**流通盘*/
	@Field(valueKey="text",binding="${quotationBean!=null && quotationBean.status==1?quotationBean.capital:null}",converter="convertorSecuamount")
	String capital;
	
	/**市值*/
	@Field(valueKey="text",binding="${quotationBean!=null && quotationBean.status==1?quotationBean.marketvalue:null}",converter="convertorSecuamount")
	String marketvalue;
	
	@Field(valueKey="visible",visibleWhen="${quotationBean!=null && quotationBean.status==2}")
	boolean stopStockTrading;
	
	@Field(valueKey="visible",visibleWhen="${quotationBean!=null && quotationBean.status==1}")
	boolean goingStockTrading;
	
	@OnShow
	protected void initData(){
	}
	
	@Command
	String handlerPageChanged(InputEvent event) {
		Object p = event.getProperty("position");
		Object s = event.getProperty("size");
		if(p  instanceof Integer) {
			this.position = (Integer)p;
			this.size = (Integer)s;
		}
		registerBean("size", size);
		registerBean("position", position);
		log.debug("GeGuStockPage handlerPageChanged position: " + position + "size: "+size);
		return null;
	}
	
	//挑战交易盘买入
	@Command(navigations={
			@Navigation(on = "tiaozhan",showPage="QuickBuyStockPage")
	})
	@SecurityConstraint(allowRoles={})
	@NetworkConstraint
	CommandResult tiaoZhanTradingBuyClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = payData();
			temp.put("tiaozhan", "tiaozhan");
			result.setPayload(temp);
			result.setResult("tiaozhan");
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
			return result;
		}
		return null;
	}
	//参数交易盘买入
	@Command(navigations={
			@Navigation(on = "cansai",showPage="QuickBuyStockPage")
	})
	@SecurityConstraint(allowRoles={})
	@NetworkConstraint
	CommandResult canSaiTradingBuyClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			CommandResult result = new CommandResult();
			HashMap<String, Object> temp = payData();
			temp.put("cansai", "cansai");
			result.setPayload(temp);
			result.setResult("cansai");
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
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
			updateSelection(new StockSelection(quotationBean.getMarket(), quotationBean.getCode(), this.stockName, 1));
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
		registerBean("size", 2);
		registerBean("position", 0);
	}
	
	@OnCreate
	void registerSelectionListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		selectionChanged("",service.getSelection(StockSelection.class));
		service.addSelectionListener(this);
	}
	
	@OnDestroy
	void removeSelectionListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		service.removeSelectionListener(this);
	}
	
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		if(selection instanceof StockSelection){
			StockSelection stockSelection = (StockSelection) selection;
			if(stockSelection!=null){
				this.codeValue = stockSelection.getCode();
				this.stockName = stockSelection.getName();
				this.marketCode = stockSelection.getMarket();
			}
			registerBean("codeValue", this.codeValue);
			registerBean("stockName", this.stockName);
			registerBean("marketCode", this.marketCode);
		}
	}
	
	@OnUICreate
	void initSearchView() {
		if(StringUtils.isBlank(marketCode) || StringUtils.isBlank(codeValue)) {
			AppUtils.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("result", 0);
					getUIContext().getWorkbenchManager().getWorkbench().showPage("stockSearchPage", map, null);
					hasShow = true;
				}
			}, 100, TimeUnit.MILLISECONDS);
		}
	}
	
	@OnShow
	void startStockSearch() {
		if(hasShow) {
			if(StringUtils.isBlank(marketCode) || StringUtils.isBlank(codeValue)) {
				hide();
			} else {
				
			}
			return;
		} else {
		}
	}
	
	@OnUIDestroy
	void destroyData() {
		hasShow = false;
		marketCode = null;
		codeValue = null;
	}
}
