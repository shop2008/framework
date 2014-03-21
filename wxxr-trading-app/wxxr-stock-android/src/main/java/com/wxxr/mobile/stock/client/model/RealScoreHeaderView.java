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
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;

@View(name="realScoreHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.user_score_header_layout")
public abstract class RealScoreHeaderView extends ViewBase {

	@Field(valueKey="text", binding="${voucherDetailsBean!=null?voucherDetailsBean.addToday:0}",attributes={
			@Attribute(name = "textColor", value = "${'resourceId:color/red'}")
			}, converter="stockL2StrConvertor")
	String todayIncrease;
	
	@Field(valueKey="text", binding="${voucherDetailsBean!=null?voucherDetailsBean.reduceToday:0}",attributes={
			@Attribute(name = "textColor", value = "${'resourceId:color/user_green_color'}")
			}, converter="stockL3StrConvertor")
	String todayDecrease;
	
	@Field(valueKey="text",binding="${voucherDetailsBean!=null?voucherDetailsBean.bal:0}",attributes={
			@Attribute(name = "textColor", value = "${'resourceId:color/white'}")
			}, converter="stockL4StrConvertor")
	String todayBalance; 
	
	@Convertor(params={
			@Parameter(name="format", value="%.0f"),
			@Parameter(name="nullString",value="0"),
			@Parameter(name="plusString", value="+")
	})
	StockLong2StringConvertor stockL2StrConvertor;
	
	@Convertor(params={
			@Parameter(name="format", value="%.0f"),
			@Parameter(name="nullString",value="0"),
			@Parameter(name="plusString", value="-")
	})
	StockLong2StringConvertor stockL3StrConvertor;
	
	@Convertor(params={
			@Parameter(name="format", value="%.0f"),
			@Parameter(name="nullString",value="0")
	})
	StockLong2StringConvertor stockL4StrConvertor;
	@Bean(type=BindingType.Pojo, express="${tradingService.getVoucherDetails(0,10)}")
	VoucherDetailsBean voucherDetailsBean;

	@Bean(type=BindingType.Service)
	ITradingManagementService tradingService;
}
