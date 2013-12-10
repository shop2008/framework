package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.model.UserLoginCallback;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "userLoginPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.login_layout")
public abstract class UserLoginPage extends PageBase {
	static Trace log = Trace.register(UserLoginPage.class);
	@Field(valueKey = "text", binding = "${callback.userName}")
	String mobileNum;

	@Field(valueKey = "text", binding = "${callback.password}")
	String password;

	@Field(valueKey = "text")
	String loginBtn;

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean
	UserLoginCallback callback = new UserLoginCallback();

	/**
	 * 处理登录
	 * 
	 * @param event
	 * @return null
	 */
	@Command(navigations = { @Navigation(on = "loginfailedexception", message = "resourceId:message/login_failed_message", params = {
			@Parameter(name = "autoclosed", type = ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "resourceId:message/login_failed_title")
			
	}) }

	)
	@ExeGuard(title = "登录中", message = "正在登录，请稍候...", silentPeriod = 1)
	String login(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			/** 手机号码 */
			// String mobileNum = mobileNumField.getValue();
			/** 密 码 */
			// String password = passwordField.getValue();

			if (log.isDebugEnabled()) {
				log.debug("login:mobileNum" + this.callback.getUserName());
				log.debug("login:password" + this.callback.getPassword());
			}
			usrService.login(this.callback.getUserName(),
					this.callback.getPassword());
			hide();
		}
		return null;

	}

	/**
	 * 处理后退
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "back")
	String back(InputEvent event) {

		// if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
		// //处理后退事件
		// getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		// }
		// this.callback.done(true);
		hide();
		return null;
	}

	// /**
	// * 手机号码编辑框
	// *
	// * @param event
	// * @return
	// */
	// @Command(commandName="mnTextChanged")
	// String mnTextChanged(InputEvent event) {
	// if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
	// String mobileNum = (String) event.getProperty("changedText");
	// mobileNumField.setValue(mobileNum);
	// }
	// return null;
	// }
	//
	//
	// /**
	// * 密码编辑框
	// * @param event
	// * @return
	// */
	// @Command(commandName="pswTextChanged")
	// String pswTextChanged(InputEvent event) {
	// if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
	// String password = (String) event.getProperty("changedText");
	// passwordField.setValue(password);
	// }
	// return null;
	// }

	/**
	 * 找回密码
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "findPasswordBack", navigations = { @Navigation(on = "OK", showPage = "userFindPswPage", closeCurrentView = true) })
	String findPasswordBack(InputEvent event) {
		// this.callback.done(true);
		return "OK";
	}

	/**
	 * 快速注册
	 * 
	 * @param event
	 * @return
	 */

	@Command(commandName = "quickRegister", navigations = { @Navigation(on = "OK", showPage = "userRegPage", closeCurrentView = true) })
	String quickRegister(InputEvent event) {
		// this.callback.done(true);
		return "OK";
	}

	@OnUIDestroy
	protected void clearData() {
		callback.setPassword("");
		callback.setUserName("");
	}
}
