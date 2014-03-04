/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

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
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
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
	String acctId;

	@Bean(type = BindingType.Service)
	ITradingManagementService tradingMgr;

	@Bean(type = BindingType.Pojo, express = "${tradingMgr.getTradingAccountRecord(acctId,0,100)}")
	BindableListWrapper<TradingRecordBean> recordListBean;
	// List

	@Field(valueKey = "options", binding = "${recordListBean != null?recordListBean.data:null}", visibleWhen="${recordListBean != null?(recordListBean.data!=null?(recordListBean.data.size()>0?true:false):false):false}")
	List<TradingRecordBean> tradingRecordBean;
	// RadioButton

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "result".equals(key)) {
					if(tempt instanceof Long) {
						acctId = (Long)tempt + "";
					} else if(tempt instanceof String) {
						acctId = (String)tempt;
					}
					registerBean("acctId", this.acctId);
				}
			}
		}
	}
}
