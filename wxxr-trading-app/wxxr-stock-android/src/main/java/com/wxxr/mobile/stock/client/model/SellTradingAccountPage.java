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
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.model.StockTradingOrder;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.Utils;

@View(name="sellTradingAccount",withToolbar=true, description="--")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.sell_trading_account_info_page_layout")
public abstract class SellTradingAccountPage extends PageBase implements IModelUpdater {

	static Trace log = Trace.getLogger(SellTradingAccountPage.class);
	
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
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${accid=selection;tradingService.getTradingAccountInfo(accid)}")
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
	List<StockTradingOrder> stockTradingOrder;
	/**交易订单
	 * 为空：按钮不可用
	 * 非空：按钮可用
	 * */
	@Field(valueKey="enabled",binding="${stockTradingOrder!=null?true:false}")
	boolean isEmpty;
	
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
		registerBean("utils", utils);
		if(getAppToolbar()!=null){
			if(this.virtual){
				getAppToolbar().setTitle("模拟盘", null);
			}else{
				getAppToolbar().setTitle("实盘", null);
			}
		}
	}
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof Map){
			Map temp = (Map)value;
	        for (Object key : temp.keySet()) {
	            boolean tempt = (Boolean) temp.get(key);
	            if ("result".equals(key)) {
	            	this.virtual = tempt;
	            	log.info("TradingMainView virtual="+virtual);
	            	registerBean("virtual", tempt);
	            }
	        }
		}
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
