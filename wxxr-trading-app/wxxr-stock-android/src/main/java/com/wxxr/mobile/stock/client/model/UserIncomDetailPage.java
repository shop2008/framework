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
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.TradeDetailBean;
import com.wxxr.mobile.stock.app.bean.TradeDetailListBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.binding.IRefreshCallback;

/**
 * 收支明细界面
 * @author renwenjie
 *
 */
@View(name="userIncomDetailPage", withToolbar=true, description="余额明细")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.income_detail_layout")
public abstract class UserIncomDetailPage extends PageBase {

	static Trace log;
	
	@Field(valueKey = "options", binding="${tradeDetailList!=null?tradeDetailList.tradeDetails:null}")
	List<TradeDetailBean> incomeDetails;
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type=BindingType.Pojo, express="${usrService!=null?usrService.myTradeDetailInfo:null}")
	TradeDetailListBean tradeDetailList;
	
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
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
	
}
