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

@View(name="actualScoreItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.actual_panel_integral_detail_item_layout")
public abstract class URealPanelScoreItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text", binding="${(scoreBean!=null&&scoreBean.comment!=null)?scoreBean.comment:'--'}")
	String scoreCatagory;
	
	@Field(valueKey="text",binding="${scoreBean!=null?scoreBean.time:null}", converter="timeConvertor")
	String gainDate;
	
	@Field(valueKey="text", binding="${scoreBean!=null?scoreBean.amount:null}",attributes={
			@Attribute(name = "textColor", value = "${scoreBean.amount>0?'resourceId:color/red':'resourceId:color/green'}")
			},converter="stockL2StrConvertor")
	String gainNum;
	
	@Convertor(params = { @Parameter(name = "format", value = "yyyy年MM月dd日"),
			@Parameter(name = "nullString", value = "--") })
	LongTime2StringConvertor timeConvertor;
	
	@Convertor(params={@Parameter(name="nullString",value="0"),
			@Parameter(name="format", value="%.0f"),
			@Parameter(name="multiple", value="1.0f")
	})
	StockLong2StringConvertor stockL2StrConvertor;
	@Override
	public void updateModel(Object value) {
		if (value instanceof GainPayDetailBean) {
			registerBean("scoreBean", value);
		}
	}
}
