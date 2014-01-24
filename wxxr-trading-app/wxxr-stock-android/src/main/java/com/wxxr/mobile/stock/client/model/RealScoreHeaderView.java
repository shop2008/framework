package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.VoucherDetailsBean;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="realScoreHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.user_score_header_layout")
public abstract class RealScoreHeaderView extends ViewBase {

	@Field(valueKey="text", binding="${voucherDetailsBean!=null?voucherDetailsBean.addToday:'--'}",attributes={
			@Attribute(name = "textColor", value = "${voucherDetailsBean.addToday>=0?'resourceId:color/red':'resourceId:color/user_green_color'}")
			}, converter="stockL2StrConvertor")
	String todayIncrease;
	
	@Field(valueKey="text", binding="${voucherDetailsBean!=null?voucherDetailsBean.reduceToday:'--'}",attributes={
			@Attribute(name = "textColor", value = "${voucherDetailsBean.reduceToday>=0?'resourceId:color/red':'resourceId:color/user_green_color'}")
			}, converter="stockL2StrConvertor")
	String todayDecrease;
	
	@Field(valueKey="text",binding="${voucherDetailsBean!=null?voucherDetailsBean.bal:'--'}",attributes={
			@Attribute(name = "textColor", value = "${voucherDetailsBean.bal>=0?'resourceId:color/red':'resourceId:color/user_green_color'}")
			}, converter="stockL2StrConvertor")
	String todayBalance; 
	
	@Convertor(params={
			
			@Parameter(name="format", value="%.0f"),
			@Parameter(name="nullString",value="0"),
			@Parameter(name="plusString", value="+")
	})
	StockLong2StringConvertor stockL2StrConvertor;
	@Bean(type=BindingType.Pojo, express="${tradingService.getVoucherDetails(0,10)}")
	VoucherDetailsBean voucherDetailsBean;

}
