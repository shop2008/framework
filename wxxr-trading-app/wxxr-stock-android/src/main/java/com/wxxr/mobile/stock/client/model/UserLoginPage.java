package com.wxxr.mobile.stock.client.model;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.model.UserLoginCallback;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "userLoginPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.login_layout")
public abstract class UserLoginPage extends PageBase {
	static Trace log = Trace.register(UserLoginPage.class);
	@Field(valueKey = "text", binding="${callBack.userName}")
	String mobileNum;

	@Field(valueKey = "text", binding="${callBack.password}")
	String password;
	
	@Field(valueKey = "text")
	String loginBtn;

	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	
	@Bean(type=BindingType.Pojo, express="${usrService!=null?usrService.createLoginCallback():null}")
	UserLoginCallback callBack;
	
	/**
	 * 处理登录
	 * @param event
	 * @return null
	 */
	@Command
	String login(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	/**
	 * 处理后退
	 * @param event
	 * @return
	 */
	@Command(commandName = "back")
	String back(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//处理后退事件
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	/**
	 * 找回密码
	 * @param event
	 * @return
	 */
	@Command(commandName="findPasswordBack", navigations={@Navigation(on="OK", showPage="userFindPswPage")})
	String findPasswordBack(InputEvent event) {
		return "OK";
	}
	
	/**
	 * 快速注册
	 * @param event
	 * @return
	 */
	@Command(commandName="quickRegister", navigations={@Navigation(on="OK", showPage="userRegPage")})
	String quickRegister(InputEvent event) {
		return "OK";
	}
	
}
