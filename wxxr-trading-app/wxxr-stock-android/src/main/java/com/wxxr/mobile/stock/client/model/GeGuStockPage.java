package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.android.app.AppUtils;
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
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUICreate;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
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
import com.wxxr.mobile.stock.app.service.IOptionStockManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="GeGuStockPage", description="个股界面",provideSelection=true,withToolbar=true)
@AndroidBinding(type=AndroidBindingType.ACTIVITY, layoutId="R.layout.gegu_page_layout")
public abstract class GeGuStockPage extends PageBase implements IModelUpdater, ISelectionChangedListener {
	
	static Trace log = Trace.getLogger(GeGuStockPage.class);
	@Menu(items = { "left","right","search" }) 
	private IMenu toolbar;

	private boolean hasShow = false;
	
	@Bean(type = BindingType.Service)
	IOptionStockManagementService optionStockService;
	
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getStockQuotation(stockSelection.getCode(), stockSelection.getMarket())}", effectingFields="f_overlay")
	StockQuotationBean quotationBean;
	
	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen="${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="right",label="添加自选股",icon="resourceId:drawable/add_stock_button_style", visibleWhen="${(quotationBean!=null && !quotationBean.added)}")
			},
			navigations = { 
				 @Navigation(on = "*", message = "成功添加自选股", params = {
							@Parameter(name = "icon", value = "resourceId:drawable/remind_focus"),
							@Parameter(name="autoClosed",type=ValueType.INETGER,value="1")
				 }) 
			})
	String toolbarClickedRight(InputEvent event) {
		if(optionStockService!=null){
			this.optionStockService.add(stockSelection.getCode(), stockSelection.getMarket());
			if(quotationBean!=null){
				quotationBean.setAdded(true);
			}
		}
		return "";
	}
	
	@Command(description="Invoke when a toolbar item was clicked",
			uiItems={
				@UIItem(id="search",label="删除自选股",icon="resourceId:drawable/del_stock_button_style", visibleWhen="${(quotationBean!=null && quotationBean.added)}")
			},
			navigations = { 
			@Navigation(on = "*", message = "成功删除自选股", params = {
						@Parameter(name = "icon", value = "resourceId:drawable/remind_focus"),
						@Parameter(name="autoClosed",type=ValueType.INETGER,value="1")
			}) 
			})
	String toolbarClickedRightSearch(InputEvent event) {
		if(optionStockService!=null){
			this.optionStockService.delete(stockSelection.getCode(), stockSelection.getMarket());
			if(quotationBean!=null){
				quotationBean.setAdded(false);
			}
		}
		return "";
	}
	
	@Bean
	boolean isAddStock;
	
/**	@Field(valueKey="visible", visibleWhen="${(quotationBean!=null && !quotationBean.added)}")
	boolean addStock;
	DataField<Boolean> addStockField;
	
	@Field(valueKey="visible", visibleWhen="${(quotationBean!=null && quotationBean.added)}")
	boolean delStock;
	DataField<Boolean> delStockField;
*/
	
	@Field
	String f_overlay;
	
	@Command
	String handleClick(InputEvent event) {
		hide();
		return null;
	}
	
/**	@Command(commandName="addStockClick",
		navigations={
			@Navigation(on = "StockAppBizException", message = "%m", params = {
					@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2")})
	})
	String addStockClick(InputEvent event) {
		if(optionStockService!=null){
			this.optionStockService.add(this.codeValue, this.marketCode);
			if(quotationBean!=null){
				quotationBean.setAdded(true);
			}
		}
		return "";
	}
	
	@Command(commandName="delStockClick",
			navigations={
				@Navigation(on = "StockAppBizException", message = "%m", params = {
						@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2")})
		})
		String delStockClick(InputEvent event) {
			if(optionStockService!=null){
				optionStockService.delete(this.codeValue, this.marketCode);
				if(quotationBean!=null){
					quotationBean.setAdded(false);
				}
			}
			return "";
		}
*/
	
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
	
	@Field(valueKey="text", attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String acctRefreshView;
	
	@Command
	String handleRefresh(InputEvent event) {
		if("TopRefresh".equals(event.getEventType())) {
			if (log.isDebugEnabled()) {
				log.debug("GeGuStockPage : getStockQuotation");
			}
			updateSelection(new StockSelection(stockSelection.getMarket(), stockSelection.getCode(), stockSelection.getName()));
			this.infoCenterService.getSyncStockQuotation(stockSelection.getCode(),stockSelection.getMarket());
		}
		return null;
	}	
	
	@Bean
	Map<String, String> map;
	
//	@Bean
//	String codeValue; //股票代码
//	 
//	@Bean
//	String marketCode; // 市场代码
//	
//	@Bean
//	String stockName;
	
	@Bean
	StockSelection stockSelection = new StockSelection();
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

	@ViewGroup(viewIds={"GeGuMinuteLineView", "StockKLineView"})
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
	
	private HashMap<String, Object> payData(){
		HashMap<String, Object> tempMap = new HashMap<String, Object>();
		if(quotationBean!=null){
			tempMap.put("code", stockSelection.getCode());
			tempMap.put("market", stockSelection.getMarket());
			tempMap.put("name", stockSelection.getName());
			updateSelection(new StockSelection(stockSelection.getMarket(), stockSelection.getCode(), stockSelection.getName(), 1));
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
					stockSelection.setCode(code);
//					this.codeValue = code;
//					registerBean("codeValue", this.codeValue);
				}
				if(key.equals("market")){
					String market = (String)data.get(key);
					stockSelection.setMarket(market);
//					this.marketCode = market;
//					registerBean("marketCode", this.marketCode);
				}
				if(key.equals("name")){
					String name = (String) data.get(key);
					stockSelection.setName(name);
//					this.stockName = name;
//					registerBean("stockName", this.stockName);
				}
	        }
			registerBean("stockSelection", stockSelection);
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
				this.stockSelection = stockSelection;
//				this.codeValue = stockSelection.getCode();
//				this.stockName = stockSelection.getName();
//				this.marketCode = stockSelection.getMarket();
			}
			registerBean("stockSelection", this.stockSelection);
//			registerBean("codeValue", this.codeValue);
//			registerBean("stockName", this.stockName);
//			registerBean("marketCode", this.marketCode);
		}
	}
	
	@OnUICreate
	void initSearchView() {
		if(StringUtils.isBlank(this.stockSelection.getMarket()) || StringUtils.isBlank(this.stockSelection.getCode())) {
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
			if(StringUtils.isBlank(this.stockSelection.getMarket()) || StringUtils.isBlank(this.stockSelection.getCode())) {
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
//		marketCode = null;
//		codeValue = null;
	}
}
