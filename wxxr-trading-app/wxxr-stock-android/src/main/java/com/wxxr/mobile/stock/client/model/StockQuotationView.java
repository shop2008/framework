package com.wxxr.mobile.stock.client.model;

import java.util.Date;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.app.service.IInfoCenterManagementService;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;

@View(name = "StockQuotationView")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.stock_quotation_view_layout")
public abstract class StockQuotationView extends PageBase implements
		IModelUpdater {

	@Bean
	String nameBean;
	@Bean
	String codeBean;
	@Bean
	String marketBean;
	@Bean
	String timeBean;;
	
	LongTime2StringConvertor longTime2StringConvertor;
	
	@Bean(type=BindingType.Service)
	IInfoCenterManagementService infoCenterService;
	
	@Bean(type=BindingType.Pojo,express="${infoCenterService.getStockQuotation(codeBean, marketBean)}")
	StockQuotationBean stockQuotationBean;
	//Title
	@Field(valueKey = "text", binding= "${nameBean}")
	String name;
	@Field(valueKey = "text", binding= "${'('}${stockQuotationBean!=null?stockQuotationBean.code:'--'}${'.'}${stockQuotationBean!=null?stockQuotationBean.market:''}${')'}")
	String code;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.newprice:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String newprice;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.change:'--'}${'('}${stockQuotationBean!=null?stockQuotationBean.risefallrate:'--'}${')'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String changeRisefallRate;
	@Field(valueKey = "imageURI", attributes={
			@Attribute(name = "imageURI", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:drawable/up_arrows':'resourceId:drawable/down_arrows'}")
			})
	String newpriceIcon;
	@Field(valueKey = "text", binding= "${timeBean}")
	String time;
	//卖
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellprice5:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String sellPrice5;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellprice4:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String sellPrice4;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellprice3:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String sellPrice3;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellprice2:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String sellPrice2;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellprice1:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String sellPrice1;
	
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume5:'--'}")
	String sellVolume5;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume4:'--'}")
	String sellVolume4;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume3:'--'}")
	String sellVolume3;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume2:'--'}")
	String sellVolume2;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.sellvolume1:'--'}")
	String sellVolume1;
	//买
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyprice5:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String buyPrice5;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyprice4:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String buyPrice4;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyprice3:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String buyPrice3;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyprice2:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String buyPrice2;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyprice1:'--'}", attributes={
			@Attribute(name = "textColor", value = "${stockQuotationBean.newprice>stockQuotationBean.close?'resourceId:color/red':'resourceId:color/green'}")
			})
	String buyPrice1;
	
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume5:'--'}")
	String buyVolume5;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume4:'--'}")
	String buyVolume4;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume3:'--'}")
	String buyVolume3;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume2:'--'}")
	String buyVolume2;
	@Field(valueKey = "text", binding= "${stockQuotationBean!=null?stockQuotationBean.buyvolume1:'--'}")
	String buyVolume1;
	//买卖盘
	@Field(valueKey = "text", binding= "${'内盘:'}${stockQuotationBean!=null?stockQuotationBean.sellsum:'--'}", attributes={
			@Attribute(name = "textColor", value = "resourceId:color/green")
			})
	String sellSum;
	@Field(valueKey = "text", binding= "${'外盘:'}${stockQuotationBean!=null?stockQuotationBean.buysum:'--'}", attributes={
			@Attribute(name = "textColor", value = "resourceId:color/red")
			})
	String buySum;
	@Override
	public void updateModel(Object value) {
		// TODO Auto-generated method stub
		registerBean("nameBean", "鸿达兴业");
		registerBean("codeBean", "100100");
		registerBean("marketBean", "SH");
		registerBean("timeBean", new Date().toGMTString());
	}
}
