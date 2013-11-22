package com.wxxr.mobile.stock.client.model;



import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.TradeDetailBean;
import com.wxxr.mobile.stock.client.utils.Float2StringConvertor;
@View(name="incomeDetailsItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.income_details_item_layout")
public abstract class UserIncomeDetailItemView extends ViewBase implements IModelUpdater {

	
	
	@Field(valueKey = "text", binding="${detailBean!=null?detailBean.tradeCatagory:'---'}")
	String incomeCatagory;

	@Field(valueKey = "text", binding="${detailBean!=null?detailBean.tradeDate:'---'}")
	String incomeDate;
	
	@Field(valueKey = "text", binding="${detailBean!=null?detailBean.tradeAmount:'---'}", converter="float2StringConvertor")
	String incomeAmount;

	TradeDetailBean detailBean;
	
	@Convertor(
			params={@Parameter(name="format", value="%+10.2f")}
			)
	Float2StringConvertor float2StringConvertor;
	@Override
	public void updateModel(Object value) {
		if (value instanceof TradeDetailBean) {
			registerBean("detailBean", value);
		}
	}
}

