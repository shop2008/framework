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

/**
 * @author duzhen
 */
@View(name="TBuyTradingPageHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.buy_trading_account_header_layout")
public abstract class TBuyTradingPageHeaderView extends ViewBase implements IModelUpdater,ISelectionChangedListener{

	private static final Trace log = Trace.register(TBuyTradingPageHeaderView.class);
	
	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
	
	@Bean(type=BindingType.Pojo,express="${tradingService.getTradingAccountInfo(accid)}")
	TradingAccountBean tradingBean;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f%%"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorSpecial;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorYuan;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f"),
			@Parameter(name="multiple",value="100")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f"),
			@Parameter(name="multiple",value="100")
	})
	StockLong2StringAutoUnitConvertor stockLong2StringAutoUnitConvertorInt;
	
	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.applyFee:''}", converter = "stockLong2StringAutoUnitConvertorInt")
	String applyFee;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.avalibleFee:''}", converter = "stockLong2StringAutoUnitConvertor")
	String avalibleFee;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.gainRate:''}", converter = "stockLong2StringConvertorSpecial", attributes={
			@Attribute(name = "textColor", value = "${tradingBean==null?'resourceId:color/gray':tradingBean.gainRate>0?'resourceId:color/stock_text_up':(tradingBean.gainRate<0?'resourceId:color/stock_text_down':'resourceId:color/gray')}")
			})
	String gainRate;

	@Field(valueKey = "text", binding = "${tradingBean!=null?tradingBean.totalGain:''}", converter = "stockLong2StringConvertorYuan", attributes={
			@Attribute(name = "textColor", value = "${tradingBean==null?'resourceId:color/gray':tradingBean.totalGain>0?'resourceId:color/stock_text_up':(tradingBean.totalGain<0?'resourceId:color/stock_text_down':'resourceId:color/gray')}")
			})
	String totalGain;
	
	@Bean
	String accid;
	
	@Bean
	boolean isVirtual = true; //true模拟盘，false实盘
	
	/**
	 * 模拟盘点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "TBuyStockInfoPage", showPage = "TBuyStockInfoPage"),
			@Navigation(on = "ShiPanBuyStockInfoPage", showPage = "ShiPanBuyStockInfoPage") })
	CommandResult handleStockClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			if (tradingBean != null) {
				if(isVirtual){
					resutl.setPayload(tradingBean.getId());
					resutl.setResult("TBuyStockInfoPage");
				}else{
					HashMap<String, Object> data = new HashMap<String, Object>();
					data.put(Constants.KEY_ASSET_TYPE, tradingBean.getType());
					data.put(Constants.KEY_ACCOUNT_ID_FLAG, tradingBean.getId());
					resutl.setPayload(data);
					resutl.setResult("ShiPanBuyStockInfoPage");
				}
			}
			return resutl;
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
				this.isVirtual = accidSelection.isVirtual();
			}
			registerBean("accid", this.accid);
			registerBean("isVirtual", this.isVirtual);
		}
	}	
	
	@Override
	public void updateModel(Object value) {

	}
}
