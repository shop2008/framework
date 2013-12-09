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

@View(name="userPicSet")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_pic_set_layout")
public abstract class UserPicSetPage extends PageBase {
	
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
	String head1Click(InputEvent event) {
		user.setUserPic("resourceId:drawable/head1");
		hide();
		return null;
	}
	
	@Command
	String head2Click(InputEvent event) {
		user.setUserPic("resourceId:drawable/head2");
		hide();
		return null;
	}
	
	@Command
	String head3Click(InputEvent event) {
		user.setUserPic("resourceId:drawable/head3");
		hide();
		return null;
	}
	
	@Command
	String head4Click(InputEvent event) {
		user.setUserPic("resourceId:drawable/head4");
		hide();
		return null;
	}
	
	@Command
	String head5Click(InputEvent event) {
		user.setUserPic("resourceId:drawable/head5");
		hide();
		return null;
	}
	
	@Command
	String head6Click(InputEvent event) {
		user.setUserPic("resourceId:drawable/head6");
		hide();
		return null;
	}
}
