package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.utils.StockString2StringConvertor1;
import com.wxxr.stock.trading.ejb.api.LossRateNDepositRate;

@View(name="DepositCashItem")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.deposit_cash_item_layout")
public abstract class DepositCashItem extends ViewBase implements IModelUpdater {


	@Bean
	LossRateNDepositRate deposit;
	
	@Bean
	float depositCash;
	
	@Convertor(params={
			@Parameter(name="format",value="%.0f%%"),
			@Parameter(name="multiple", value="100f"),
			@Parameter(name="nullString",value="--")
	})
	StockString2StringConvertor1 stockLong2StringConvertorSpecial;
	
	@Field(valueKey="text",binding="${depositCash!=null?depositCash:'--'}",converter="stockLong2StringConvertorSpecial")
	String moneyValue;
	
	@Override
	public void updateModel(Object data) {
		if(data instanceof LossRateNDepositRate){
			LossRateNDepositRate temp = (LossRateNDepositRate) data;
			this.depositCash = Float.parseFloat(temp.getDepositCash());
			registerBean("depositCash", depositCash);
		}
	}
}
