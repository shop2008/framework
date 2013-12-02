package com.wxxr.mobile.stock.client.model;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IAppToolbar;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

/**
 * 他人T买入界面
 * @author renwenjie
 *
 */
@View(name="otherUserBuyInPage", withToolbar=true, description="参赛交易盘")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.other_user_buy_in_layout")
public abstract class OtherUserBuyInPage extends PageBase implements IModelUpdater {
	@Field(valueKey="enabled", binding="${virtual}")
	boolean virtualEnabled;
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getTradingAccountInfo(accId)}")
	TradingAccountBean tradingAccount;
	
	
	@Convertor(params = { @Parameter(name="format",value="M月d日买入")})
	LongTime2StringConvertor lt2BuySConvertor;

	@Convertor(params = { @Parameter(name="format",value="M月d日卖出")})
	LongTime2StringConvertor lt2SellSConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockL2StrConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple",value="100")
	})
	StockLong2StringAutoUnitConvertor autoUnitConvertor;
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor float2StringConvertor;

	
	/**买入日期  */
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.buyDay:'--月--日买入'}", converter="lt2BuySConvertor")
	String buyDay;  
	
	/**卖出日期 */
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.sellDay:'--月--日卖出'}", converter="lt2SellSConvertor")
	String sellDay;  
	
	/**申购金额*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.applyFee:0}", converter="autoUnitConvertor")
	String applyFee;
	
	/**可用资金*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.avalibleFee:0}", converter="autoUnitConvertor")
	String avalibleFee;
	
	/**总盈亏率*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.gainRate:0}",attributes={
			@Attribute(name = "textColor", value = "${(tradingAccount!=null && tradingAccount.gainRate>0)?'resourceId:color/red':((tradingAccount!=null && tradingAccount.gainRate<0)?'resourceId:color/green':'resourceId:color/white')}")
			}, converter="stockL2StrConvertor")
	String gainRate;  
	
	/**总盈亏额*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.totalGain:0}",attributes={
			@Attribute(name = "textColor", value = "${(tradingAccount!=null && tradingAccount.totalGain>0)?'resourceId:color/red':((tradingAccount!=null && tradingAccount.totalGain<0)?'resourceId:color/green':'resourceId:color/white')}")
			}, converter="float2StringConvertor")
	String totalGain;
	
	/**交易订单列表*/
	@Field(valueKey="options",binding="${tradingAccount!=null?tradingAccount.tradingOrders:null}")
	List<StockTradingOrderBean> stockTradingOrder;
	/**交易订单
	 * 为空：按钮不可用
	 * 非空：按钮可用
	 * */
	@Field(valueKey="visible",visibleWhen="${stockTradingOrder!=null?false:true}")
	boolean isEmpty;
	
	@Field(attributes= {@Attribute(name = "enablePullDownRefresh", value= "true"),
			@Attribute(name = "enablePullUpRefresh", value= "false")})
	String acctRefreshView;
	
	/**交易盘状态 CLOSED-已结算；UNCLOSE-未结算,CLEARING-正在结算*/
	private String over;
	
	
	/**1:表示T日交易盘,0:T+1日交易盘*/
	private int status;	
	
	@Bean
	long accId;
	@Menu(items = { "left", "right" })
	private IMenu toolbar;
	
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);	
		return null;
	}
	

	
	/**
	 * 订单详情点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "right", label = "交易记录", icon = "resourceId:drawable/jyjl") }, 
			navigations = { @Navigation(on = "*", showPage = "TradingRecordsPage") })
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

	
	@Command
	String handleTopRefresh(InputEvent event) {
		
		IRefreshCallback cb = (IRefreshCallback) event.getProperty("callback");
		tradingService.getTradingAccountInfo(String.valueOf(accId));
		if (cb != null)
			cb.refreshSuccess();
		return null;
	}
	
	
	@Override
	public void updateModel(Object value) {
		Map<String, Object> map = (Map<String, Object>) value;
		Long accId = (Long)map.get("accId");
		if (accId != null) {
			this.accId = accId;
			registerBean("accId", accId);
		}
		
		Boolean isVirtual = (Boolean) map.get("isVirtual");
		
		if (isVirtual != null) {
			IAppToolbar toolbar = getAppToolbar();
			if (toolbar!=null) {
				if (isVirtual) {
					toolbar.setTitle("参赛交易盘", null);
				} else {
					toolbar.setTitle("挑战交易盘", null);
				}
			}
			registerBean("virtual", isVirtual);
		}
		
		String userId = (String) map.get("userId");
		if (!StringUtils.isBlank(userId)) {
			registerBean("userId", userId);
		}
	}
	
}
