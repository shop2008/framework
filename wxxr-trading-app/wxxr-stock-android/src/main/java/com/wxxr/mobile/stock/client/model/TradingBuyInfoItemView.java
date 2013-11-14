package com.wxxr.mobile.stock.client.model;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.TradingAccountBean;

@View(name="TradingBuyInfoItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.trading_buy_info_item")
public abstract class TradingBuyInfoItemView extends ViewBase implements IModelUpdater {
	private static final Trace log = Trace.register(TradingMainView.class);
	
	TradingAccountBean trading;
	int red = 0xffff0000;
	int green;
	//股票名称
	@Field(valueKey="text",binding="${trading!=null?trading.stockName:'--'}",attributes={@Attribute(name = "textColor", value = "resourceId:color/yellow")})
	String stockName;
	
	//股票代码
	@Field(valueKey="text",binding="${trading!=null?trading.stockCode:'--'}")
	String stockCode;
	
	//额度（申请资金）
	@Field(valueKey="text",binding="${trading!=null?trading.initCredit:'--'}")
	String initCredit;
	
	//总收益
	@Field(valueKey="text",binding="${trading!=null?trading.income:'--'}")
	String income;
	
	@Field(valueKey="enabled",binding="${trading!=null && trading.type==0?false:true}")
	boolean type;//交易盘类型  0-模拟盘；1-实盘
	
	@Command
	String testButtonClick(InputEvent event){
		return null;
	}
	
	
	@Override
	public void updateModel(Object data) {
		if(data instanceof TradingAccountBean){
//			trading = (TradingAccountBean) data;
			registerBean("trading", data);
			
//			this.stockName = trading.getStockName();
//			this.stockNameField.setValue(this.stockName);
//			this.stockCode = trading.getStockCode();
//			this.stockCodeField.setValue(this.stockCode);
//			this.initCredit = String.valueOf(trading.getInitCredit());
//			this.initCreditField.setValue(this.initCredit);
//			float income = trading.getIncome();
//			if(income>0){
//				incomeField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_RED);
//			}else if(income<0){
//				incomeField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GREEN);
//			}else{
//				incomeField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_WHITE);
//			}
//			
//			if(trading.getStatus()==1){
//				DataField<String> stockNameField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GRAY);
//				stockCodeField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GRAY);
//				initCreditField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GRAY);
//				incomeField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GRAY);
//			}
//			this.income = String.valueOf(trading.getIncome());
//			this.incomeField.setValue(this.income);
//			if(trading.getType()==0){
//				type = false;
//			}else if(trading.getType()==1){
//				type = true;
//			}
//			this.typeField.setValue(this.type);
		}
	}
}
