/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;

/**
 * @author duzhen
 * 
 */
@View(name = "TBuyStockOrdersPage", withToolbar = true, description = "交易记录")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.buy_stock_orders_page_layout")
public abstract class TBuyStockOrdersPage extends PageBase implements
		IModelUpdater {
	private static final Trace log = Trace.register(TBuyStockOrdersPage.class);

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingMgr;

	@Bean(type = BindingType.Pojo, express = "${tradingMgr.getDealDetail()}")
	DealDetailBean dealDetailBean;
	// List

	@Field(valueKey = "options", binding = "${dealDetailBean != null?tradingRecords}")
	List<TradingRecordBean> tradingRecordBean;
	// RadioButton

	// week Title
	@Field(valueKey = "text", binding = "${dealDetailBean!=null?(ChampionShipBean.weekRanKBeans!=null?(ChampionShipBean.weekRanKBeans.size()>0?ChampionShipBean.weekRanKBeans.get(0).dates:''):''):''}", visibleWhen = "${currentViewId == 3}")
	String weekDate;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@OnShow
	protected void initStockOrders() {

	}

	@Override
	public void updateModel(Object value) {

	}

}
