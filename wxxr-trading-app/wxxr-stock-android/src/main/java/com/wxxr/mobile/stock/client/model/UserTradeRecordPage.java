package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="userTradeRecordPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_trade_record_page_layout")
public abstract class UserTradeRecordPage extends PageBase {

	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type=BindingType.Pojo,express="${usrService.fetchUserInfo}")
	UserBean user;
	
	@Field(valueKey="visible", binding="${user.allTradeRecords!=null?true:false}")
	boolean recordNotNullVisible;
	
	@Field(valueKey="visible", binding="${user.allTradeRecords==null?true:false}")
	boolean recordNullVisible;
	
	
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
	
	
}
