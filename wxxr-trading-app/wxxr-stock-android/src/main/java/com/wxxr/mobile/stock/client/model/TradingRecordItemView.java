package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;

@View(name = "TradingRecordItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.deal_record_layout_item")
public abstract class TradingRecordItemView extends ViewBase implements
		IModelUpdater {
	@Bean
	TradingRecordBean recordBean;
	
	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.date:'--'}")
	String date;
	
	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.date:'--'}")
	String time;

	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.market:'--'}")
	String market;
	
	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.code:'--'}")
	String code;
	
	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.describe:'--'}", attributes={
			@Attribute(name = "textColor", value = "${recordBean.beDone?'resourceId:color/red':'resourceId:color/gray'}")
			})
	String describe;

	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.price:'--'}")
	String price;

	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.vol:'--'}")
	String vol;

	@Override
	public void updateModel(Object value) {
		if (value instanceof TradingRecordBean) {
			registerBean("recordBean",value);
		}
	}
}
