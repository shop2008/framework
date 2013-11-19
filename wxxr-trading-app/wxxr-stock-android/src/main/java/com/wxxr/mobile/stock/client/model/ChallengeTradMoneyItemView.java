package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;


@View(name="challengeTradeMoneyItem")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.challenge_trade_money_item")
public abstract class ChallengeTradMoneyItemView extends ViewBase implements IModelUpdater{

	@Field(valueKey="text")
	String moneyTxt;
	DataField<String> moneyTxtField;
	
	
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof String){
			String val = (String) value;
			this.moneyTxt = val;
			this.moneyTxtField.setValue(this.moneyTxt);
		}
	}
}
