package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.bean.TradeDetailBean;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;
import com.wxxr.mobile.stock.client.service.IUserManagementService;

/**
 * 收支明细界面
 * @author renwenjie
 *
 */
@View(name="userIncomDetailPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.income_detail_layout")
public abstract class UserIncomDetailPage extends PageBase {

	static Trace log;
	
	@Field(valueKey = "options")
	List<TradeDetailBean> incomeDetails;
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	
	DataField<List> incomeDetailsField;
	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "back", description = "Back To Last UI")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator()
					.hidePage(this);
		}
		return null;
	}
	
	@Command
	String handleTopRefresh(InputEvent event) {
		if(log.isDebugEnabled()) {
			log.debug("ChampionShipView : handleTMegaTopRefresh");
		}
		IRefreshCallback cb = (IRefreshCallback)event.getProperty("callback");
		//ChampionShip.clear();
		//handleTMegaClick(null);
		if(cb!=null)
			cb.refreshSuccess();
		return null;
		
	}

	@OnCreate
	void injectServices() {
		this.usrService = AppUtils.getService(IUserManagementService.class);
	}
	
	@OnShow
	void showView() {
		
		this.incomeDetails = new ArrayList<TradeDetailBean>();
		
		for(int i=0;i<4;i++) {
			TradeDetailBean detail = new TradeDetailBean();
			detail.setTradeAmount("100");
			detail.setTradeCatagory("充入现金");
			detail.setTradeDate("2013-11-13");
			this.incomeDetails.add(detail);
		}
		
		this.incomeDetailsField.setValue(this.incomeDetails);
	}
	
}
