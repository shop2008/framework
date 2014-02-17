package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockQuotationBean;
import com.wxxr.mobile.stock.client.widget.StockInfoBean;

@View(name = "InfoCenterEdittemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.price_info_center_edit_item_view")
public abstract class InfoCenterEdittemView extends ViewBase implements IModelUpdater {

//	@Bean(type = BindingType.Service)
//	IStockInfoSyncService stockInfoSyncService;
//		
//	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(stockTaxis.code, stockTaxis.market)}")
//	StockBaseInfo stockInfoBean;
//	
	@Bean
	StockQuotationBean stockTaxis;
	
	@Bean
	StockInfoBean StockInfo;
	
	@Field(valueKey="text",binding="${stockTaxis!=null?stockTaxis.stockName:'--'}")
	String stockName;//股票名称
	
	@Field(valueKey="text",binding="${stockTaxis!=null?stockTaxis.code:'--'}")
	String stockCode;//股票代码
	
	//勾选框
	@Command
	String handleIsDelChange(InputEvent event){
		return null;
	}
	
	//置顶
	@Command
	String handleGoTopClick(InputEvent event){
		return null;
	}
	//删除
	@Command
	String handleDelClick(InputEvent event){
		return null;
	}
	
	//拖动
	@Command
	String handleDrage(InputEvent event){
		return null;
	}
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof StockQuotationBean) {
			registerBean("stockTaxis", data);
		}
		if(data instanceof StockInfoBean){
			registerBean("StockInfo", data);
		}
	}
}
