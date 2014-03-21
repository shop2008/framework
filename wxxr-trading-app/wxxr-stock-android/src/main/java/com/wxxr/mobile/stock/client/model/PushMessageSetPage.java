package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUICreate;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="PushMessageSetPage",withToolbar=true, description="推送设置")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.push_mesage_setting")
public abstract class PushMessageSetPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type=BindingType.Pojo,express="${usrService.myUserInfo}")
	UserBean user;
	
	@Field(valueKey="checked",binding="${user!=null?user.messagePushSettingOn:false}", enableWhen="${isLogined}")
	boolean pushMsgEnabled;
	
	@Field(valueKey="enabled", binding="${isLogined==false}")
	boolean pushMsgBodyEnabled;
	
	@Menu(items = {"left"})
	protected IMenu toolbar;
	
	Boolean isChecked = null;
	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen="${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	
	boolean isLogined = false;
	
	@OnUICreate
	void initData() {
		isLogined = AppUtils.getService(IUserIdentityManager.class).isUserAuthenticated();
		registerBean("isLogined", isLogined);
	}
	
	@Command
	String pushMsgStatusChanged(ExecutionStep step, InputEvent event, Object result) {
		switch (step) {
		case PROCESS:
			Object obj = event.getProperty("isChecked");
			
			if (obj instanceof Boolean) {
				isChecked = (Boolean) obj;
			}
			
			if (isChecked != null) {
				usrService.pushMessageSetting(isChecked.booleanValue());
			}
			break;

		case NAVIGATION:
			if(user != null) {
				user.setMessagePushSettingOn(isChecked);
				registerBean("user", user);
			}
			break;
		default:
			break;
		}
		return null;
	}
	
	@Command(navigations={@Navigation(on="+", showDialog="UnLoginDialogView")})
	String pushMsgBodyClick(InputEvent event) {
		return "+";
	}
}
