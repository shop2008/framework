package com.wxxr.mobile.stock.client.model;



import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;


@View(name="userHomeSet")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_home_set_layout")
public abstract class UserHomeBackSetPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;

	@Command
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	
	@Command
	String back1Click(InputEvent event) {
		user.setHomeBack("resourceId:drawable/back1");
		hide();
		return null;
	}
	
	@Command
	String back2Click(InputEvent event) {
		user.setHomeBack("resourceId:drawable/back2");
		hide();
		return null;
	}
	
	@Command
	String back3Click(InputEvent event) {
		user.setHomeBack("resourceId:drawable/back3");
		hide();
		return null;
	}
	
	@Command
	String back4Click(InputEvent event) {
		user.setHomeBack("resourceId:drawable/back4");
		hide();
		return null;
	}
	
	@Command
	String back5Click(InputEvent event) {
		user.setHomeBack("resourceId:drawable/back5");
		hide();
		return null;
	}
	
	@Command
	String back6Click(InputEvent event) {
		user.setHomeBack("resourceId:drawable/back6");
		hide();
		return null;
	}
}
