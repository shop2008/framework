/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
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
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.StockSelection;
import com.wxxr.mobile.stock.client.utils.Constants;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.mobile.stock.client.utils.Utils;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

/**
 * 买入
 * BuyStockDetailInputView需要输入的价格和资金，来计算最大股数setHint；价格在逻辑视图处理*1000传入
 * StockInputKeyboard需要昨收价计算%和最大股数计算比例
 * 
 * @author duzhen
 * 
 */
@View(name = "BuyStockDetailPage", withToolbar = true, description="买入", provideSelection=true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.buy_stock_detail_layout")
public abstract class BuyStockDetailPage extends PageBase implements
		IModelUpdater,ISelectionChangedListener {
	@ViewGroup(viewIds={"StockQuotationView", "GZMinuteLineView", "StockKLineView"})
	private IViewGroup contents;
	
	private static final Trace log = Trace.register(BuyStockDetailPage.class);
	private boolean hasShow = false;
	private boolean isMarket = false;
	//记录输入价格记录
	String orderPriceInput = "";
	
	@Bean
	String acctIdBean;
	
	@Bean
	String marketBean;
	
	@Bean
	String nameBean;
	
	@Bean
	String codeBean;
	//传进去，只有StockInputKeyboard键盘计算涨跌幅，传昨收价
	@Bean
	String marketPriceBean;
	
	@Bean
	String amountBean;
	//资金，算能买多少股
	@Bean
	String avalibleFeeBean;
	
	//键盘输入值和点了市价后改变；传入BuyStockDetailInputView计算可买股数使用
	@Bean
	String orderPriceBean;
	
	@Bean
	boolean isVirtual = true; //true模拟盘，false实盘
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getStockQuotation(codeBean, marketBean)}")
	StockQuotationBean stockQuotationBean;

	// 查股票名称
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;

	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(codeBean, marketBean)}")
	StockBaseInfo stockInfoBean;
		
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple", value="1000.00")
	})
	StockLong2StringConvertor stockLong2StringConvertor;
	
	@Field(valueKey = "text", attributes = {
			@Attribute(name = "type", value = "0"),
			@Attribute(name = "orderPrice", value = "${orderPriceBean}"),
			@Attribute(name = "marketPrice", value = "${marketPriceBean}"),
			@Attribute(name = "fund", value = "${avalibleFeeBean}") })
	String inputView;
	
	@Field(valueKey = "text", binding="${stockInfoBean!=null?stockInfoBean.name:''}${' '}${codeBean}")
	String stock;
	DataField<String> stockField;
	
	@Field(valueKey = "text", binding="${stockQuotationBean!=null?stockQuotationBean.newprice:null}", converter="stockLong2StringConvertor", 
			attributes={@Attribute(name = "enabled", value="${stockQuotationBean!=null&&stockQuotationBean.status!=2}")})
	String price;
	DataField<String> priceField;
	
	@Field(valueKey = "text", binding="${amountBean}", 
			attributes={@Attribute(name = "enabled", value="${stockQuotationBean!=null&&stockQuotationBean.status!=2}")})
	String count;
	
	@Field(valueKey = "enabled", attributes={
			@Attribute(name = "backgroundImageURI", value = "${isVirtual?'resourceId:drawable/buy_button_bule_btn':'resourceId:drawable/buy_button_red_btn'}")
			})
	boolean buyBtn = true;
	
	@Field(valueKey = "visible")
	boolean progress;
	DataField<Boolean> progressField;

	@Field(valueKey = "visible")
	boolean refresh = true;
	DataField<Boolean> refreshField;
	
	@Field(valueKey = "visible")
	boolean keyboard = true;
	DataField<Boolean> keyboardField;
	
	@Bean
	int size;
	@Bean
	int position;
	
	@Field(valueKey = "text", attributes = {
			@Attribute(name = "size", value = "${size}"),
			@Attribute(name = "position", value = "${position}") })
	String indexGroup;
	//停牌
	@Field(valueKey = "text", visibleWhen = "${stockQuotationBean!=null&&stockQuotationBean.status!=2}")
	String inputLayout;
	@Field(valueKey = "text", visibleWhen = "${stockQuotationBean!=null&&stockQuotationBean.status==2}")
	String inputSuspension;
	
	@Field(valueKey = "visible")
	boolean refreshSus = true;
	DataField<Boolean> refreshSusField;
	@Field(valueKey = "visible")
	boolean progressSus;
	DataField<Boolean> progressSusField;
	
	
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Command(navigations = {@Navigation(on = "stockSearchPage", showPage = "stockSearchPage")})
	CommandResult handlerStockClicked(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult result = new CommandResult();
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("result", 1);
			result.setResult("stockSearchPage");
			result.setPayload(map);
			return result;
		}
		return null;
	}
	
	@Command
	String handlerRefreshClicked(InputEvent event) {
		if(stockQuotationBean!=null) {
			long price = stockQuotationBean.getNewprice();
			String val = String.format("%.2f", price/1000f);
//			String val = Utils.roundHalfUp(price/1000f, 2)+"";
			priceField.setValue("");
			priceField.setValue(val);
			stockField.setValue("");
			stockField.setValue(stockInfoBean.getName() + " " + codeBean);
		}
		infoCenterService.getStockQuotation(codeBean, marketBean);
		//同步
		StockQuotationBean bean = infoCenterService.getSyncStockQuotation(codeBean, marketBean);
		this.stockQuotationBean = bean;
		registerBean("stockQuotationBean", this.stockQuotationBean);
		orderPriceBean = bean.getNewprice() + "";
		marketPriceBean = bean.getClose() + "";
		registerBean("orderPriceBean", orderPriceBean);
		registerBean("marketPriceBean", marketPriceBean);
		updateSelection(new StockSelection(this.marketBean,this.codeBean,this.nameBean, 1));
		return null;
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
		log.debug("BuyStockDetailPage handlerPageChanged position: " + position + "size: "+size);
		return null;
	}
	
	/**
	 * 市价点中，作收价*1.1传进去计算最大可买股数
	 * @param envent
	 * @return
	 */
	@Command
	String handleMarketBtnClick(InputEvent envent) {
		orderPriceBean = (long) Utils.roundUp(stockQuotationBean.getClose()*1.1f, 0) + "";
		registerBean("orderPriceBean", orderPriceBean);
		isMarket = true;
		return null;
	}
	/**
	 * 挂单点中，将之前输的数值再注册给orderPriceBean,传进keyboard里面，之前传了作收*1.1
	 * @param envent
	 * @return
	 */
	@Command
	String handleOrderBtnClick(InputEvent envent) {
		orderPriceBean = orderPriceInput;
		registerBean("orderPriceBean", orderPriceBean);
		
		isMarket = false;
		return null;
	}
	@Command
	String priceTextChanged(InputEvent event) {
		String key = (String) event.getProperty("changedText");
		String value = "0";
		try {
			if(!StringUtils.isEmpty(key)){
				value = (long) Utils.roundUp(Float.parseFloat(key) * 1000, 0) + "";
			}else{
				value = key;
			}
			orderPriceBean = value;
			orderPriceInput = value;
			registerBean("orderPriceBean", value);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		marketPriceBean = stockQuotationBean.getClose() + "";
		registerBean("marketPriceBean", marketPriceBean);
		return null;
	}
	
	@Command
	String countTextChanged(InputEvent event) {
		String key = (String) event.getProperty("changedText");
		amountBean = key;
		registerBean("amountBean", key);
		return null;
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
				this.codeBean = stockSelection.getCode();
				this.nameBean = stockSelection.getName();
				this.marketBean = stockSelection.getMarket();
			}
			registerBean("codeBean", this.codeBean);
			registerBean("nameBean", this.nameBean);
			registerBean("marketBean", this.marketBean);
		}
	}

	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			String code = "";
			String name = "";
			String market = "";
			String acctId = "";
			String avalibleFee = "";
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
//				if (tempt != null && "code".equals(key)) {
//					if(tempt instanceof String) {
//						code = (String)tempt;
//					}
//					codeBean = code;
//					registerBean("codeBean", code);
//				} else if (tempt != null && "name".equals(key)) {
//					if(tempt instanceof String) {
//						name = (String)tempt;
//					}
//					nameBean = name;
//					registerBean("nameBean", name);
//				} else if (tempt != null && "market".equals(key)) {
//					if(tempt instanceof String) {
//						market = (String)tempt;
//					}
//					marketBean = market;
//					registerBean("marketBean", market);
//				} else 
				if (tempt != null && "acctId".equals(key)) {
					if(tempt instanceof String) {
						acctId = (String)tempt;
					}
					acctIdBean = acctId;
					registerBean("acctIdBean", acctId);
				} else if (tempt != null && "avalible".equals(key)) {
					if(tempt instanceof String) {
						avalibleFee = (String)tempt;
					}
					avalibleFeeBean = avalibleFee;
					registerBean("avalibleFeeBean", avalibleFee);
				} else if (tempt != null && Constants.KEY_VIRTUAL_FLAG.equals(key)) {
					if(tempt instanceof Boolean) {
						isVirtual = (Boolean)tempt;
					}
					registerBean("isVirtual", isVirtual);
				}
				log.debug("BuyStockDetailPage updateModel: avalibleFeeBean : "+avalibleFeeBean);
			}
		}
		registerBean("size", 3);
		registerBean("position", 0);
	}
	
	/**
	 * 买入点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName="handleBuyBtnClick",navigations = { 
			@Navigation(on = "StockAppBizException", message = "%m", params = {
					@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2")})				
			}
	)
	@ExeGuard(title="提示", message="正在获取数据，请稍后...", silentPeriod=1, cancellable=false)
	String handleBuyBtnClick(InputEvent event) {
		String price = null;
		if(!isMarket) {
			try {
				if(orderPriceBean!=null && !StringUtils.isEmpty(orderPriceBean))
				price = Long.parseLong(orderPriceBean)/10 + "";
				tradingService.buyStock(acctIdBean, marketBean, codeBean, price, amountBean);
			}catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}else{
			tradingService.buyStock(acctIdBean, marketBean, codeBean, "0", amountBean);
		} 
		IView v = (IView)event.getProperty(InputEvent.PROPERTY_SOURCE_VIEW);
		if(v != null)
			v.hide();
		return "";
	}
	
	@OnCreate
	protected void initStock() {
		
	}
	
	@OnShow
	void startStockSearch() {
		if(hasShow) {
			if(StringUtils.isBlank(marketBean) || StringUtils.isBlank(codeBean)) {
				hide();
			} else {
				
			}
			return;
		} else {
		}
		if(StringUtils.isBlank(marketBean) || StringUtils.isBlank(codeBean)) {
			AppUtils.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("result", 1);
					getUIContext().getWorkbenchManager().getWorkbench().showPage("stockSearchPage", map, null);
					hasShow = true;
				}
			}, 100, TimeUnit.MILLISECONDS);
		}
	}
	
	@OnHide
	void setKeyboardHide() {
		keyboardField.setValue(false);
	}
	
	@OnUIDestroy
	void destroyData() {
		isMarket = false;
		hasShow = false;
		marketBean = null;
		codeBean = null;
		amountBean = null;
		acctIdBean = null;
		marketPriceBean = null;
		avalibleFeeBean = null;
		orderPriceBean = null;
		registerBean("marketBean", "");
		registerBean("codeBean", "");
		registerBean("amountBean", "");
		registerBean("acctIdBean", "");
		registerBean("marketPriceBean", "");
		registerBean("avalibleFeeBean", "");
		registerBean("orderPriceBean", "");
	}
}
