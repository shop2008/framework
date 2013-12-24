package com.wxxr.mobile.stock.client.model;

import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.event.api.IBroadcastEvent;
import com.wxxr.mobile.core.event.api.IEventListener;
import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.event.NewRemindingMessagesEvent;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="SellTradingAccountHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.sell_trading_listview_header_page_layout")
public abstract class SellTradingAccountHeaderView extends ViewBase implements IModelUpdater,ISelectionChangedListener, IEventListener  {

	static Trace log = Trace.getLogger(SellTradingAccountHeaderView.class);
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getTradingAccountInfo(accid)}")
	TradingAccountBean tradingAccount;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="multiple",value="100"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple",value="100"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor1;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorYuan;	

	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="100.00"),
			@Parameter(name="nullString",value="--")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;	
	/** 交易盘编号*/
	private long id;
	
	/**申购金额*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.applyFee:null}",converter="stockLong2StringAutoUnitConvertor")
	String applyFee;
	
	/**可用资金*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.avalibleFee:null}",converter="stockLong2StringAutoUnitConvertor1")
	String avalibleFee;
	
	/**总盈亏率*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.gainRate:null}",converter="stockLong2StringConvertorSpecial",attributes={
			@Attribute(name = "textColor", value = "${(tradingAccount!=null && tradingAccount.gainRate>0)?'resourceId:color/red':((tradingAccount!=null && tradingAccount.gainRate<0)?'resourceId:color/green':'resourceId:color/white')}")
			})
	String gainRate;  
	
	/**总盈亏额*/
	@Field(valueKey="text",binding="${tradingAccount!=null?tradingAccount.totalGain:null}", converter = "stockLong2StringConvertorYuan" ,attributes={
			@Attribute(name = "textColor", value = "${(tradingAccount!=null && tradingAccount.totalGain>0)?'resourceId:color/red':((tradingAccount!=null && tradingAccount.totalGain<0)?'resourceId:color/green':'resourceId:color/white')}")
			})
	String totalGain;
	
	
	@Field(valueKey="enabled",enableWhen="${virtual}")
	boolean dateTitle;
	@Bean
	String accid;
	
	/**是否为模拟盘*/
	@Bean
	boolean virtual;
	
	//消息推送
	@Field(valueKey = "text")
	String message;
	DataField<String> messageField;
	
	@Field(valueKey = "visible")
	boolean messageLayout;
	DataField<Boolean> messageLayoutField;
	
	@Field(valueKey = "text")
	String closeBtn;
	
	@Command(navigations = { 
			@Navigation(on = "TBuyStockInfoPage", showPage = "TBuyStockInfoPage"),
			@Navigation(on = "ShiPanBuyStockInfoPage", showPage = "ShiPanBuyStockInfoPage") 
			})
	CommandResult handleStockClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			if (tradingAccount != null) {
				resutl.setPayload(tradingAccount.getId());
				if(tradingAccount.getVirtual()){
					resutl.setResult("TBuyStockInfoPage");
				}else{
					resutl.setResult("ShiPanBuyStockInfoPage");
				}
				return resutl;
			}
		}
		return null;
	}
	
	@OnShow
	void registerEventListener() {
		AppUtils.getService(IEventRouter.class).registerEventListener(NewRemindingMessagesEvent.class, this);
	}
	
	@Override
	public void onEvent(IBroadcastEvent event) {
		if(tradingService != null)
			tradingService.getTradingAccountInfo(accid);
		NewRemindingMessagesEvent e = (NewRemindingMessagesEvent) event;
		final RemindMessageBean[] messages = e.getReceivedMessages();

		final int[] count = new int[1];
		count[0] = 0;
		final Runnable[] tasks = new Runnable[1];
		tasks[0] = new Runnable() {

			@Override
			public void run() {
				if (messages != null && count[0] < messages.length) {
					RemindMessageBean msg = messages[count[0]++];
					messageField.setValue(msg.getCreatedDate() + "，"
							+ msg.getTitle() + "，" + msg.getContent());
					messageLayoutField.setValue(true);
					AppUtils.runOnUIThread(tasks[0], 5, TimeUnit.SECONDS);
				}
			}
		};
		if (messages != null)
			AppUtils.runOnUIThread(tasks[0], 5, TimeUnit.SECONDS);
	}
	
	@OnHide
	void unRegisterEventListener() {
		AppUtils.getService(IEventRouter.class).unregisterEventListener(NewRemindingMessagesEvent.class, this);
	}
	
	@OnCreate
	void registerSelectionListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		selectionChanged("",service.getSelection(AccidSelection.class));
		service.addSelectionListener(this);
	}
	
	@OnDestroy
	void removeSelectionListener() {
		ISelectionService service = getUIContext().getWorkbenchManager().getWorkbench().getSelectionService();
		service.removeSelectionListener(this);
	}
	
	@Override
	public void selectionChanged(String providerId, ISelection selection) {
		if(selection instanceof AccidSelection){
			AccidSelection accidSelection = (AccidSelection) selection;
			if(accidSelection!=null){
				this.accid = accidSelection.getAccid();
				this.virtual = accidSelection.isVirtual();
			}
			registerBean("accId", this.accid);
			registerBean("isVirtual", this.virtual);
		}
	}	
	
	@Override
	public void updateModel(Object value) {

	}
}
