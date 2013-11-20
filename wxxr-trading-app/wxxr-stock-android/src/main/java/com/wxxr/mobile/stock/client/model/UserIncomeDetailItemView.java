package com.wxxr.mobile.stock.client.model;



import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.TradeDetailBean;
import com.wxxr.mobile.stock.client.utils.Utils;
@View(name="incomeDetailsItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.income_details_item_layout")
public abstract class UserIncomeDetailItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey = "text", binding="${detailBean!=null?detailBean.tradeCatagory:'---'}")
	String incomeCatagory;

	@Field(valueKey = "text", binding="${detailBean!=null?detailBean.tradeDate:'---'}")
	String incomeDate;
	
	@Field(valueKey = "text", binding="${detailBean!=null?util.parseFloat(detailBean.tradeAmount):'---'}")
	String incomeAmount;

	TradeDetailBean detailBean;
	
	@Bean
	Utils util = Utils.getInstance();
	
	@OnShow
	protected void initData() {
		registerBean("util", util);
	}
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof TradeDetailBean) {
			registerBean("detailBean", value);
		}
	}
}

