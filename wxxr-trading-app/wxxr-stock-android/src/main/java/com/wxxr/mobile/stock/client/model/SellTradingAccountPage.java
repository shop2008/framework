package com.wxxr.mobile.stock.client.model;

import java.util.List;
import java.util.Map;

import android.widget.Toast;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
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
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;
import com.wxxr.mobile.stock.client.utils.Utils;

@View(name="sellTradingAccount",withToolbar=true, description="--")
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
	
	@Bean
	String accid;
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getTradingAccountInfo(accid)}")
	TradingAccountBean tradingAccount;
	
	/** 交易盘编号*/
	private long id;
	
	/**买入日期  */
	@Field(valueKey="text",binding="${tradingAccount!=null?utils.getDate(tradingAccount.buyDay):'--'}${'买入'}")
	String buyDay;  
	
	/**卖出日期 */
	@Field(valueKey="text",binding="${tradingAccount!=null?utils.getDate(tradingAccount.sellDay):'--'}${'卖出'}")
	String sellDay;  
	
	/**申购金额*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.applyFee:'--'}${'万'}")
	String applyFee;
	
	/**可用资金*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.avalibleFee:'--'}${'元'}")
	String avalibleFee;
	
	/**总盈亏率*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.gainRate:'--'}",attributes={
			@Attribute(name = "textColor", value = "${(tradingAccount!=null && tradingAccount.gainRate>0)?'resourceId:color/red':((tradingAccount!=null && tradingAccount.gainRate<0)?'resourceId:color/green':'resourceId:color/white')}")
			})
	String gainRate;  
	
	/**总盈亏额*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.totalGain:'--'}${'元'}",attributes={
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
	@Field(valueKey="enabled",binding="${stockTradingOrder!=null?true:false}")
	boolean isEmpty;
	
	@Field(valueKey="text")
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
		registerBean("utils", utils);
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
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "right", label = "交易详情", icon = "resourceId:drawable/jyjl") }, navigations = { @Navigation(on = "*", showPage = "TradingRecordsPage") })
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
	
	
	@Command(navigations = { @Navigation(on = "TBuyStockInfoPage", showPage = "TBuyStockInfoPage") })
	CommandResult handleStockClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			if (tradingAccount != null) {
				resutl.setPayload(tradingAccount.getId());
			}
			resutl.setResult("TBuyStockInfoPage");
			return resutl;
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
	        	}
	        }
	        log.info("TradingMainView virtual="+virtual+"accid="+accid);
	        registerBean("virtual", virtual);
	        registerBean("accid", accid);
		}
	}
	
	@Command
	String handleTopRefresh(InputEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleTMegaTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		tradingService.getTradingAccountInfo(accid);
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}
	
	
	//卖出
	@Command(navigations={@Navigation(on="sell",showPage="sellTrading")})
	CommandResult sellTradingAction(InputEvent event){
		Toast.makeText(AppUtils.getFramework().getAndroidApplication(), "确定需要卖出吗？", Toast.LENGTH_SHORT).show();
		CommandResult result = new CommandResult();
		result.setResult("sell");
		return result;
	}
	//清仓
	@Command
	String cleanTradingAction(InputEvent event){
		Toast.makeText(AppUtils.getFramework().getAndroidApplication(), "确定需要清仓吗？", Toast.LENGTH_SHORT).show();
		return null;
	}
}
