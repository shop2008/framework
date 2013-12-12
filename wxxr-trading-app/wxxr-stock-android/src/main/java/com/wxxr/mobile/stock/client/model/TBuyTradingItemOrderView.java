package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.utils.StockLong2StringConvertor;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;

@View(name = "TBuyTradingItemOrderView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.buy_trading_stock_order_item")
public abstract class TBuyTradingItemOrderView extends ViewBase implements
		IModelUpdater {
	// 交易服务
	@Bean(type = BindingType.Service)
	ITradingManagementService manageService;

	// 查股票名称
	@Bean(type = BindingType.Service)
	IStockInfoSyncService stockInfoSyncService;

	@Bean(type = BindingType.Pojo, express = "${stockInfoSyncService.getStockBaseInfoByCode(orderBean!=null?orderBean.stockCode:'', orderBean!=null?orderBean.marketCode:'')}")
	StockBaseInfo stockInfoBean;

	@Bean
	StockTradingOrderBean orderBean;

	@Convertor(params = { @Parameter(name = "format", value = "%.2f元"),
			@Parameter(name = "multiple", value = "100.00") })
	StockLong2StringConvertor stockLong2StringConvertorNoSign;

	@Field(valueKey = "text", binding = "${stockInfoBean!=null?stockInfoBean.name:'--'}")
	String stockName;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.buy:'0'}", converter = "stockLong2StringConvertorNoSign")
	String buy;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.stockCode:'--'}")
	String stockCode;

	@Field(valueKey = "text", binding = "${orderBean!=null?orderBean.amount:'--'}${'股'}")
	String amount;

	@Field(valueKey = "text", binding = "${orderBean!=null?(orderBean.status=='PROCESSING'?'撤  单':(orderBean.status=='100'?'正在撤单':'正在撤单')):'撤  单'}")
	String status;

	@Menu(items={"left","right","search"})
	IMenu toolbar;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof StockTradingOrderBean) {
			orderBean = (StockTradingOrderBean) value;
			registerBean("orderBean", value);
		}
	}

	@Command(uiItems=
			@UIItem(id="right",label="取消",icon="resourceId:drawable/zpb")
		,navigations = { @Navigation(on = "*", showDialog = "messageBox", params = {
			@Parameter(name = "title", value = ""),
			@Parameter(name = "icon", value = "resourceId:drawable/remind_focus"),
			@Parameter(name = "message", value = "resourceId:message/confirm_cancel_order"),
			@Parameter(name = "onOK", value = "leftok"),
			@Parameter(name = "onCanceled", value = "right") }) })
	String handlerCancelClick(InputEvent event) {
		// 未实现撤单前确认
		// if("100".equals(orderBean.getStatus()))
		// return null;
		// manageService.cancelOrder(orderBean.getId()+"");
		// orderBean.setStatus("100");
		return "";
	}

	@Command(uiItems=@UIItem(id="leftok",label="确定",icon="resourceId:drawable/home"))
	String confirmCancelClick(InputEvent event) {
		// 未实现撤单前确认
		if ("100".equals(orderBean.getStatus()))
			return null;
		manageService.cancelOrder(orderBean.getId() + "");
		orderBean.setStatus("100");
		return null;
	}
}
