package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name = "UserIncomeDetailItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.income_details_item_layout")
public abstract class UserIncomeDetailItemView extends ViewBase implements
		IModelUpdater {

	@Field(valueKey = "text", binding = "${(detailBean!=null&&detailBean.comment!=null)?detailBean.comment:'--'}")
	String incomeCatagory;

	@Field(valueKey = "text", binding = "${detailBean!=null?detailBean.time:null}", converter = "timeConvertor")
	String incomeDate;

	@Field(valueKey = "text", binding = "${detailBean!=null?detailBean.amount:null}", 
			attributes={
			@Attribute(name="textColor", value="${detailBean!=null?(detailBean.amount>0?'resourceId:color/red':'resourceId:color/green'):'resourceId:color/white'}")
	}, converter="stockL2StrConvertor")
	String incomeAmount;

	GainPayDetailBean detailBean;

	@Convertor(params = { @Parameter(name = "format", value = "yyyy年MM月d日"),
			@Parameter(name = "nullString", value = "--") })
	LongTime2StringConvertor timeConvertor;

	
	@Convertor(params={@Parameter(name="nullString",value="0"),
			@Parameter(name="format", value="%.2f"),
			@Parameter(name="multiple", value="100.0f"),
			@Parameter(name="formatUnit", value="元")
	})
	StockLong2StringConvertor stockL2StrConvertor;
	@Override
	public void updateModel(Object value) {
		if (value instanceof GainPayDetailBean) {
			registerBean("detailBean", value);
		}
	}
}
