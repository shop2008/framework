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

@View(name="realScoreItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.actual_panel_integral_detail_item_layout")
public abstract class URealPanelScoreItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text", binding="${(scoreBean!=null&&scoreBean.comment!=null)?scoreBean.comment:'--'}")
	String scoreCatagory;
	
	@Field(valueKey="text",binding="${scoreBean!=null?scoreBean.time:null}", converter="timeConvertor")
	String gainDate;
	
	@Field(valueKey="text", binding="${scoreBean!=null?scoreBean.amount:null}",attributes={
			@Attribute(name = "textColor", value = "${scoreBean.amount>0?'resourceId:color/red':'resourceId:color/user_green_color'}")
			},converter="stockL2StrConvertor")
	String gainNum;
	
	@Convertor(params = { @Parameter(name = "format", value = "yyyy-MM-dd"),
			@Parameter(name = "nullString", value = "xxxx-xx-xx") })
	LongTime2StringConvertor timeConvertor;
	
	@Convertor(params={@Parameter(name="nullString",value="0"),
			@Parameter(name="format", value="%.0f"),
			@Parameter(name="multiple", value="1.0f"),
			@Parameter(name="plusString", value="+")
	})
	StockLong2StringConvertor stockL2StrConvertor;
	@Override
	public void updateModel(Object value) {
		if (value instanceof GainPayDetailBean) {
			registerBean("scoreBean", value);
		}
	}
}
