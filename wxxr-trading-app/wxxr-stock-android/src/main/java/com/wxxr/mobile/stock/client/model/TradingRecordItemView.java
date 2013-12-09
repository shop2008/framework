package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.client.utils.LongTime2StringConvertor;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

@View(name = "TradingRecordItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.deal_record_layout_item")
public abstract class TradingRecordItemView extends ViewBase implements
		IModelUpdater {
	// 查股票名称
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;

	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(recordBean!=null?recordBean.code:'', recordBean!=null?recordBean.market:'')}")
	StockBaseInfo stockInfoBean;

	@Bean
	TradingRecordBean recordBean;
	
	@Convertor(params={
			@Parameter(name="format",value="HH:mm:ss")
	})
	LongTime2StringConvertor longTime2StringConvertor;
	
	@Convertor(params={
			@Parameter(name="format",value="%.2f元"),
			@Parameter(name="multiple", value="100.00")
	})
	StockLong2StringConvertor stockLong2StringConvertorYuan;
	
	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.date:'--'}", visibleWhen="false")
	String date;
	
	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.date:''}", converter = "longTime2StringConvertor")
	String time;

	@Field(valueKey = "text", binding = "${stockInfoBean!=null?stockInfoBean.name:'--'}")
	String name;
	
	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.code:'--'}")
	String code;
	
	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.describe:'--'}", attributes={
			@Attribute(name = "textColor", value = "${recordBean.beDone?'resourceId:color/red':'resourceId:color/gray'}")
			})
	String describe;

	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.price:''}", converter = "stockLong2StringConvertorYuan")
	String price;

	@Field(valueKey = "text", binding = "${recordBean!=null?recordBean.vol:'--'}${'股'}")
	String vol;

	@Override
	public void updateModel(Object value) {
		if (value instanceof TradingRecordBean) {
			recordBean = (TradingRecordBean)value;
			registerBean("recordBean",value);
		}
	}
}
