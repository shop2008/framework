package com.wxxr.mobile.stock.client.model;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.TradingAccountBean;
import com.wxxr.mobile.stock.client.utils.ColorUtils;

@View(name="TradingBuyInfoItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.trading_buy_info_item")
public abstract class TradingBuyInfoItemView extends ViewBase implements IModelUpdater {
	private static final Trace log = Trace.register(TradingMainView.class);
	//股票名称
	@Field(valueKey="text")
	String stockName;
	DataField<String> stockNameField;
	
	//股票代码
	@Field(valueKey="text")
	String stockCode;
	DataField<String> stockCodeField;
	
	//额度（申请资金）
	@Field(valueKey="text")
	String initCredit;
	DataField<String> initCreditField;
	
	//总收益
	@Field(valueKey="text")
	String income;
	DataField<String> incomeField;
	
	@Field(valueKey="enabled")
	boolean type;//交易盘类型  0-模拟盘；1-实盘
	DataField<Boolean> typeField;
	
	@Command
	String testButtonClick(InputEvent event){
//		Context context = AppUtils.getFramework().getAndroidApplication();
//		Toast.makeText(context, "你点击了Button按钮", Toast.LENGTH_SHORT).show();
		log.info("testButtonClick+++++++++++++++++++");
		return null;
	}
	
	@Override
	public void updateModel(Object data) {
		if(data instanceof TradingAccountBean){
			TradingAccountBean trading = (TradingAccountBean) data;
			this.stockName = trading.getStockName();
			this.stockNameField.setValue(this.stockName);
			this.stockCode = trading.getStockCode();
			this.stockCodeField.setValue(this.stockCode);
			this.initCredit = String.valueOf(trading.getInitCredit());
			this.initCreditField.setValue(this.initCredit);
			float income = trading.getIncome();
			if(income>0){
				incomeField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_RED);
			}else if(income<0){
				incomeField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GREEN);
			}else{
				incomeField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_WHITE);
			}
			
			if(trading.getStatus()==1){
				stockNameField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GRAY);
				stockCodeField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GRAY);
				initCreditField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GRAY);
				incomeField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GRAY);
			}
			this.income = String.valueOf(trading.getIncome());
			this.incomeField.setValue(this.income);
			if(trading.getType()==0){
				type = false;
			}else if(trading.getType()==1){
				type = true;
			}
			this.typeField.setValue(this.type);
//			this.status = trading.getStatus();
//			incomeField.setAttribute(key, val)
		}
	}
}
