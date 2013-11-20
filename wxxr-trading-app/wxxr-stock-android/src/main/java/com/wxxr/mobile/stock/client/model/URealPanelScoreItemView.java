package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ScoreBean;

@View(name="actualScoreItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.actual_panel_integral_detail_item_layout")
public abstract class URealPanelScoreItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text", binding="${scoreBean!=null?scoreBean.catagory:'--'}")
	String scoreCatagory;
	
	@Field(valueKey="text",binding="${scoreBean!=null?scoreBean.date:'--'}")
	String gainDate;
	
	@Field(valueKey="text", binding="${scoreBean!=null?scoreBean.amount:'--'}",attributes={
			@Attribute(name = "textColor", value = "${scoreBean.amount>0?'resourceId:color/red':'resourceId:color/green'}")
			})
	String gainNum;
	
	ScoreBean scoreBean;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof ScoreBean) {
			registerBean("scoreBean", value);
		}
	}
}
