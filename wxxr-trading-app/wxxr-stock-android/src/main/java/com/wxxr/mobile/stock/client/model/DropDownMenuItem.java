/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

/**
 * @author xijiadeng
 *
 */
@View(name="dropDownMenuItem")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.new_drop_down_menu_item")
public abstract class DropDownMenuItem extends ViewBase implements IModelUpdater {

	@Bean(type = BindingType.Service)
	ITradingManagementService manageService;
		
	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(orderBean!=null?orderBean.stockCode:'', orderBean!=null?orderBean.marketCode:'')}")
	StockBaseInfo stockInfoBean;
	
	@Bean
	StockTradingOrderBean orderBean;
	
	@Field(valueKey="text",binding="${stockInfoBean!=null?stockInfoBean.name:'--'}")
	String name;
	
	@Field(valueKey="text",binding="${orderBean!=null?orderBean.stockCode:'--'}")
	String code;
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof StockTradingOrderBean){
			registerBean("orderBean", value);
		}
	}
}
