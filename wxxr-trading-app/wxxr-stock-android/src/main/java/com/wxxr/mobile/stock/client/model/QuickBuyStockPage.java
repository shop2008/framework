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
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.StockMinuteKBean;
import com.wxxr.mobile.stock.app.bean.StockMinuteLineBean;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.mobile.stock.client.utils.Utils;


@View(name="QuickBuyStockPage", description="快速买入",withToolbar=true)
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.quick_buy_page")
public abstract class QuickBuyStockPage extends PageBase implements IModelUpdater {
	static Trace log = Trace.getLogger(QuickBuyStockPage.class);
	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	@Bean(type = BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type=BindingType.Service)
	ITradingManagementService userCreateService;
	
	@Bean(type=BindingType.Pojo,express="${userCreateService.getUserCreateTradAccInfo()}")
	UserCreateTradAccInfoBean userCreateTradAccInfo;	
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getMinuteline(minuteMap)}")
	StockMinuteKBean minute;
	
	@Bean(type = BindingType.Pojo, express = "${infoCenterService.getStockQuotation(codeValue,marketValue)}")
	StockQuotationBean stockQuotation;
	
	//从上个页面传递过来的股票代码
	@Bean
	String codeValue;
	
	//从上个页面传递过来的市场代码
	@Bean
	String marketValue;
	@Bean
	String stockName;
	
	@Bean
	HashMap<String, String> minuteMap;
	
	@Bean
	int currentViewId = 0;
	@Bean
	int currentRadioBtnId = 1;
	/**综合费用比例,手续费*/
	float costRate;	
	
	/**保证金比例*/
	float depositRate;
	int changeMoney = 0;
	@Bean
	String zhfzf; //交易综合费应支付
	@Bean 
	String djDeposit; //风险保证金应冻结 or 实盘积分应支付
	float djMoney;
	
	@Bean
	String T_buyNum; //挑战交易盘买入数量
	
	@Bean
	String C_buyNum; // 参赛交易盘买入数量
	
	@ViewGroup(viewIds={"StockQuotationView","GZMinuteLineView", "StockKLineView"})
	private IViewGroup contents;
	
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
	
	@Convertor(params={
			@Parameter(name="multiple",value="1000"),
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringAutoUnitConvertor;
	
	// 股票或指数 代码+市场代码
	@Field(valueKey="text",binding="${'('}${stockQuotation!=null?stockQuotation.code:'--'}${'.'}${stockQuotation!=null?stockQuotation.market:'--'}${')'}")
	String codeMarket;
	
	//股票名称
	@Field(valueKey="text",binding="${stockName!=null?stockName:'--'}")
	String name;
	
	@Field(valueKey="text",binding="${stockName!=null?stockName:'--'}${' '}${stockQuotation!=null?stockQuotation.code:'--'}")
	String nameCode;
	
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
	
	//最新价
	@Field(valueKey="text",binding="${stockQuotation!=null?stockQuotation.newprice:null}",converter="stockLong2StringAutoUnitConvertor")
	String newprice;
	
	//申购金额
	@Field(valueKey="options",binding="${(userCreateTradAccInfo!=null&&userCreateTradAccInfo.requestamount!=null)?userCreateTradAccInfo.requestamount:null}")
	List<String> money;

	/**参数交易盘-中止止损*/
	@Field(valueKey="text",binding="${userCreateTradAccInfo.rateString3!=null?userCreateTradAccInfo.rateString3:'--'}")
	String capitalRate;
	
	/**止损*/
	@Field(valueKey="text",binding="${'余额创建'}${userCreateTradAccInfo.rateString1!=null?userCreateTradAccInfo.rateString1:'--'}${'止损'}",attributes={
			@Attribute(name = "checked", value = "${currentRadioBtnId==1?true:false}")
	})
	String rate1;
	
	/**止损*/
	@Field(valueKey="text",binding="${'余额创建'}${userCreateTradAccInfo.rateString2!=null?userCreateTradAccInfo.rateString2:'--'}${'止损'}",attributes={
			@Attribute(name = "checked", value = "${currentRadioBtnId==2?true:false}")
	})
	String rate2;
	
	/**止损*/
	@Field(valueKey="text",binding="${'积分创建'}${userCreateTradAccInfo.rateString3!=null?userCreateTradAccInfo.rateString3:'--'}${'止损'}",attributes={
			@Attribute(name = "checked", value = "${currentRadioBtnId==3?true:false}")
	})
	String rate3;	
	
	@Field(attributes={
			@Attribute(name = "checked", value = "${currentViewId==0?true:false}")
	})
	String cansaiRadionBtn;
	
	@Field(valueKey="visible",visibleWhen="${currentViewId==0}")
	boolean showCanSai;
	
	@Field(attributes={
			@Attribute(name = "checked", value = "${currentViewId==1?true:false}")
	})
	String tiaozhanRadionBtn;
	
	@Field(valueKey="visible",visibleWhen="${currentViewId==1}")
	boolean showTiaoZhan;

	@Field(valueKey="visible",visibleWhen="${currentRadioBtnId != 3}")
	boolean assure_visibility;
	
	@Field(valueKey="visible",visibleWhen="${currentRadioBtnId==3}")
	boolean jifen_visibility;
	
	//交易综合费
	@Field(valueKey="text",binding="${zhfzf!=null?zhfzf:'0.00元'}")
	String jiaoyi_zhf;
	
	//冻结余额
	@Field(valueKey="text",binding="${djDeposit!=null?djDeposit:'0.00元'}")
	String frozen_money;
	
	//挑战交易盘买入数量
	@Field(valueKey="text",binding="${T_buyNum!=null?T_buyNum:'***'}${'股'}")
	String TbuyNum;
	
	//参赛交易盘买入数量
	@Field(valueKey="text",binding="${C_buyNum!=null?C_buyNum:'***'}${'股'}")
	String CbuyNum;
	
	@Field(valueKey="enabled",enableWhen="${(stockQuotation!=null && stockQuotation.newprice!=null)}")
	boolean tiaozhanButton = true;
	
	@Field(valueKey="enabled",enableWhen="${(stockQuotation!=null && stockQuotation.newprice!=null)}")
	boolean cansaiButton = true;
	
	@OnShow
	protected void initData(){
		this.currentRadioBtnId = 1;
		registerBean("currentRadioBtnId", this.currentRadioBtnId);
		if(stockQuotation!=null){
			long mBuyNum = buyNumber(100000l, stockQuotation.getClose());
			this.C_buyNum = String.valueOf(mBuyNum);
			registerBean("C_buyNum", this.C_buyNum);
		}
	}
	
	//切换RadioButtn事件
	@Command
	String rateClick1(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			currentRadioBtnId = 1;
			registerBean("currentRadioBtnId", currentRadioBtnId);
			updataRate1();
		}
		return null;
	}
	@Command
	String rateClick2(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			currentRadioBtnId = 2;
			registerBean("currentRadioBtnId", currentRadioBtnId);
			updataRate2();
		}
		return null;
	}
	
	@Command
	String rateClick3(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			currentRadioBtnId = 3;
			registerBean("currentRadioBtnId", currentRadioBtnId);
			updataRate3();
		}
		return null;
	}	

	private float getDeposit1(){
		   return  userCreateTradAccInfo.getDeposit1();
		}
		private float getDeposit2(){
		       return  userCreateTradAccInfo.getDeposit2();
		}
		private float getDeposit3(){
	        return  userCreateTradAccInfo.getDeposit3();
	    }
		
		private float getRate1(){
		    return  userCreateTradAccInfo.getRateData1();
		}
		private float getRate2(){
	        return  userCreateTradAccInfo.getRateData2();
	    }
		private float getRate3(){
	        return  userCreateTradAccInfo.getRateData3();
	    }	
	
	private void updataRate1(){
		if(userCreateTradAccInfo!=null){
			costRate = userCreateTradAccInfo.getCostRate();
			if(changeMoney>0 && userCreateTradAccInfo.getDeposit1()>0 && costRate>0){
				zhfzf = String.format("%.2f", ((changeMoney*10000) * costRate))+"元";
				djDeposit = String.format("%.0f", (changeMoney*10000 * userCreateTradAccInfo.getDeposit1()))+"元";
				djMoney = (changeMoney*10000 * userCreateTradAccInfo.getDeposit1());
				registerBean("zhfzf", zhfzf);
				registerBean("djDeposit", djDeposit);
			}	
		}
	}
	
	private void updataRate2(){
		if(userCreateTradAccInfo!=null){
			costRate = userCreateTradAccInfo.getCostRate();
			if(changeMoney>0 && userCreateTradAccInfo.getDeposit2()>0 && costRate>0){
				zhfzf = String.format("%.2f", ((changeMoney*10000) * costRate))+"元";
				djDeposit = String.format("%.0f", ((changeMoney*10000) * userCreateTradAccInfo.getDeposit2()))+"元";
				djMoney = ((changeMoney*10000) * userCreateTradAccInfo.getDeposit2());
				registerBean("zhfzf", zhfzf);
				registerBean("djDeposit", djDeposit);
			}	
		}
	}
	private void updataRate3(){
		if(userCreateTradAccInfo!=null){
			costRate = userCreateTradAccInfo.getCostRate();
			if(changeMoney>0 && costRate>0){
				zhfzf = String.format("%.2f", ((changeMoney*10000) * costRate))+"元";
				djDeposit = String.format("%.0f", (changeMoney*10000.0))+"元";
				djMoney = changeMoney*10000;
				registerBean("zhfzf", zhfzf);
				registerBean("djDeposit", djDeposit);
			}	
		}
	}	
	//参赛tab
	@Command()
	String csQuickBuyClick(InputEvent event){
		this.currentViewId = 0;
		registerBean("currentViewId", this.currentViewId);
		return null;
	}
	//挑战Tab
	@Command()
	String tzQuickBuyClick(InputEvent event){
		this.currentViewId = 1;
		registerBean("currentViewId", this.currentViewId);
		return null;
	}
	@Command
	String moneyTextChanged(InputEvent event){
		if(InputEvent.EVENT_TYPE_TEXT_CHANGED.equals(event.getEventType())){
			Object changedText = event.getProperty("changedText");
			if(changedText instanceof String){
				String money = (String)changedText;
				String cMoney = money.substring(0, money.length()-1);
				changeMoney = Integer.parseInt(cMoney);
				if(stockQuotation!=null){
					long buyNum = buyNumber(changeMoney*10000, stockQuotation.getClose());
					this.T_buyNum = String.valueOf(buyNum);
					registerBean("T_buyNum", this.T_buyNum);
				}
				if(userCreateTradAccInfo!=null){
					costRate = userCreateTradAccInfo.getCostRate();
				}
				switch(currentRadioBtnId){
				case 1:
					updataRate1();
					break;
				case 2:
					updataRate2();
					break;
				case 3:
					updataRate3();
					break;
				}
			}
		}
		return null;
	}
	
	/**参赛交易盘买人*/
	@Command
	String CanSaiBuyStockClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			String market = null;
			String code = null;
			String stockBuyAmount = null; //委托价
			long maxBuyNum = 0; //委托数量
			long close = 0;
			if(stockQuotation!=null){
				market = stockQuotation.getMarket();
				code = stockQuotation.getCode();
				stockBuyAmount = String.valueOf(stockQuotation.getNewprice());
				close = stockQuotation.getClose();
				maxBuyNum = buyNumber(100000l, close);
			}
//			userCreateService.createTradingAccount(10000000l, getRate3(), true, getDeposit3(), "CASH");
			userCreateService.quickBuy(10000000l, String.valueOf(getRate3()), true, market, code, stockBuyAmount, String.valueOf(maxBuyNum));
		}
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return "home";	}
	
	/**挑战交易盘买人*/
	/**
	 * 快速买入
	  * @param captitalAmount
	 *            -申请额度
	 * @param capitalRate
	 *            -中止止损
	 * @param virtual
	 *            - 是否为虚拟盘 - true表示虚拟盘；false表示实盘
	 * @param depositRate - 保证金
	 * @param stockMarket - 市场代码： SH，SZ各代表上海，深圳
	 * @param stockCode -股票代码
	 * @param stockBuyAmount -委托价
	 * @param depositRate -委托数量
	 * @throws StockAppBizException
	 */
	@Command
	String TiaoZhanBuyStockClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			long captitalAmount = changeMoney * 10000 * 100; //-申请额度
			float _rate = 0.0f; //-中止止损
			float _depositRate = 0.0f; //保证金
			long close = 0; //昨收价
			String code = null;
			String market = null;
			long maxBuyNum = 0; //委托数量
			String assetType = "VOUCHER";
			String stockBuyAmount = null; //委托价
			if(stockQuotation!=null){
				stockBuyAmount = String.valueOf(stockQuotation.getNewprice());
				close = stockQuotation.getClose();
				maxBuyNum = buyNumber(changeMoney * 10000, close);
				code = stockQuotation.getCode();
				market = stockQuotation.getMarket();
			}
			switch(currentRadioBtnId){
			case 1:
				if(getRate1()>0 && getDeposit1()>0 ){
					_rate = getRate1();
					_depositRate = getDeposit1();
				}
				assetType = "CASH"; // 现金
				break;
			case 2:
				if(getRate2()>0 && getDeposit2()>0 ){
					_rate = getRate2();
					_depositRate = getDeposit2();
				}
				assetType = "CASH";
				break;
			case 3:
				if(getRate3()>0 && getDeposit3()>0 ){
					_rate = getRate3();
					_depositRate = getDeposit3();
				}
				assetType= "VOUCHER"; //积分
				break;
			}
			userCreateService.quickBuy(captitalAmount, String.valueOf(_rate), false, market, code, stockBuyAmount, String.valueOf(maxBuyNum));
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return "home";		
	}
	
	
	private Long buyNumber(long captitalAmount,long close){
		long maxBuyNum=0;
		float tValue = Utils.roundUp(close*1.1f/1000, 2);
		long num = (long) (captitalAmount/tValue);
		if(num<100){
			maxBuyNum = 0;
		}else{
			maxBuyNum =(long) (num/100*100);
		}
		return maxBuyNum;
	}
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof Map){
			Map data = (Map) value;
			for(Object key:data.keySet()){
				if(key.equals("cansai")){
					this.currentViewId = 0;
					registerBean("currentViewId", this.currentViewId);
				}
				if(key.equals("tiaozhan")){
					this.currentViewId = 1;
					registerBean("currentViewId", this.currentViewId);
				}
				if(key.equals("code")){
					String code = (String)data.get(key);
					this.codeValue = code;
					registerBean("codeValue", this.codeValue);
				}
				if(key.equals("market")){
					String market = (String)data.get(key);
					this.marketValue = market;
					registerBean("marketValue", this.marketValue);
				}
				if(key.equals("name")){
					String name = (String) data.get(key);
					this.stockName = name;
					registerBean("stockName", this.stockName);
				}
			}
			if(this.codeValue!=null && this.marketValue!=null){
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put(codeValue, marketValue);
				this.minuteMap = temp;
				registerBean("minuteMap", this.minuteMap);
			}
		}
	}
}
