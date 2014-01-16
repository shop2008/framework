package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
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
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.ISelection;
import com.wxxr.mobile.core.ui.api.ISelectionChangedListener;
import com.wxxr.mobile.core.ui.api.ISelectionService;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.biz.AccidSelection;
import com.wxxr.mobile.stock.client.utils.Constants;
import com.wxxr.mobile.stock.client.utils.StockLong2StringAutoUnitConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="SellTradingAccountHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.sell_trading_listview_header_page_layout")
public abstract class SellTradingAccountHeaderView extends ViewBase implements IModelUpdater,ISelectionChangedListener{

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
	
	
	@Command(navigations = { 
			@Navigation(on = "TBuyStockInfoPage", showPage = "TBuyStockInfoPage"),
			@Navigation(on = "ShiPanBuyStockInfoPage", showPage = "ShiPanBuyStockInfoPage") 
			})
	CommandResult handleStockClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			if (tradingAccount != null) {
				if(tradingAccount.getVirtual()){
					resutl.setPayload(tradingAccount.getId());
					resutl.setResult("TBuyStockInfoPage");
				}else{
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put(Constants.KEY_ASSET_TYPE, tradingAccount.getType());
					data.put(Constants.KEY_ACCOUNT_ID_FLAG, tradingAccount.getId());
					resutl.setPayload(data);
					resutl.setResult("ShiPanBuyStockInfoPage");
				}
				return resutl;
			}
		}
		return null;
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
