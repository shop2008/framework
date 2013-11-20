/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordListBean;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;

/**
 * @author duzhen
 * 
 */
@View(name = "TradingRecordsPage", withToolbar = true, description = "交易记录")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.buy_trading_records_page_layout")
public abstract class TradingRecordsPage extends PageBase implements
		IModelUpdater {
	private static final Trace log = Trace.register(TradingRecordsPage.class);

	@Bean
	String accId;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingMgr;

	@Bean(type = BindingType.Pojo, express = "${tradingMgr.getTradingAccountRecord(accId,0,100)}")
	TradingRecordListBean recordListBean;
	// List

	@Field(valueKey = "options", binding = "${recordListBean != null?recordListBean.getRecords():null}")
	List<TradingRecordBean> tradingRecordBean;
	// RadioButton

	// week Title
	@Field(valueKey = "text")
	// , binding =
	// "${tradingRecordBean!=null?(tradingRecordBean.size()>0?(tradingRecordBean.get(1).date):'--'):'--'}")
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
		registerBean("accId", "");
	}

	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "result".equals(key)) {
					if(tempt instanceof Long) {
						accId = (Long)tempt + "";
					} else if(tempt instanceof String) {
						accId = (String)tempt;
					}
				}
			}
		}
	}

	/**
	 * 订单列表点击
	 * 
	 * @param event
	 * @return
	 */
	@Command(navigations = { @Navigation(on = "TradingRecordOrderDetailPage", showPage = "TradingRecordOrderDetailPage"),
							 @Navigation(on = "TradingRecordDoneDetailPage", showPage = "TradingRecordDoneDetailPage")})
	CommandResult handleItemClick(InputEvent event) {
		if (InputEvent.EVENT_TYPE_ITEM_CLICK.equals(event.getEventType())) {
			CommandResult resutl = new CommandResult();
			Map map = new HashMap();
			if (event.getProperty("position") instanceof Integer) {
				int position = (Integer) event.getProperty("position");
				List<TradingRecordBean> record = (recordListBean != null?recordListBean.getRecords():null);
				if (record != null && record.size() > 0) {
					TradingRecordBean recordBean = record.get(position);
					if (recordBean != null) {
						map.put("market", recordBean.getMarket());
						map.put("code", recordBean.getCode());
						map.put("describe", recordBean.getDescribe());
						map.put("date", recordBean.getDate());
						map.put("price", recordBean.getPrice());
						map.put("vol", recordBean.getVol());
						if(recordBean.getBeDone()) {
							map.put("amount", recordBean.getAmount());
							map.put("brokerage", recordBean.getBrokerage());
							map.put("tax", recordBean.getTax());
							map.put("fee", recordBean.getFee());
							resutl.setResult("TradingRecordDoneDetailPage");
						} else {
							resutl.setResult("TradingRecordOrderDetailPage");
						}
					}
				}
			}
			resutl.setPayload(map);
			return resutl;
		}
		return null;
	}
}
