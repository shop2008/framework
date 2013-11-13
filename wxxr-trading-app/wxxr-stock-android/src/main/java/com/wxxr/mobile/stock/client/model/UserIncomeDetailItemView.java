package com.wxxr.mobile.stock.client.model;


import android.R.color;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.TradeDetailBean;
import com.wxxr.mobile.stock.client.utils.ColorUtils;
@View(name="incomeDetailsItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.income_details_item_layout")
public abstract class UserIncomeDetailItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey = "text")
	String incomeCatagory;

	@Field(valueKey = "text")
	String incomeDate;
	
	@Field(valueKey = "text")
	String incomeAmount;
	
	DataField<String> incomeCatagoryField;
	DataField<String> incomeDateField;
	
	DataField<String> incomeAmountField;
	
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof TradeDetailBean) {
			TradeDetailBean detail = (TradeDetailBean) value;
			String tradeCatagory = detail.getTradeCatagory();
			this.incomeCatagory = tradeCatagory;
			this.incomeCatagoryField.setValue(tradeCatagory);
			String tradeDate = detail.getTradeDate();
			this.incomeDate = tradeDate;
			this.incomeDateField.setValue(tradeDate);
			String tradeAmount = detail.getTradeAmount();
			
			if (Float.parseFloat(tradeAmount) > 0) {
				this.incomeAmount = "+"+tradeAmount;
				this.incomeAmountField.setValue("+"+tradeAmount);
				this.incomeAmountField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_RED);
			} else {
				this.incomeAmount = tradeAmount;
				this.incomeAmountField.setValue(tradeAmount);
				this.incomeAmountField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GREEN);
			}
			
		}
	}
}

