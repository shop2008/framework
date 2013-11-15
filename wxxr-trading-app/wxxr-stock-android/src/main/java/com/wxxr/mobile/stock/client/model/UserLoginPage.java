package com.wxxr.mobile.stock.client.model;

import javax.security.auth.login.LoginException;

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
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "userLoginPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.login_layout")
public abstract class UserLoginPage extends PageBase {
	static Trace log = Trace.register(UserLoginPage.class);
	@Field(valueKey = "text")
	String mobileNum;

	@Field(valueKey = "text")
	String password;
	
	@Field(valueKey = "text")
	String loginBtn;

	DataField<String> mobileNumField;

	DataField<String> passwordField;
	
	DataField<String> loginBtnField;

	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	
	/**
	 * 处理登录
	 * @param event
	 * @return null
	 */
	@Command(commandName = "login")
	String login(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			/**手机号码*/
			String mobileNum = mobileNumField.getValue();
			/**密 码*/
			String password = passwordField.getValue();
			
			if (log.isDebugEnabled()) {
				log.debug("login:mobileNum"+mobileNum);
				log.debug("login:password"+password);
			}
			
			usrService.login(mobileNum, password);
			
		}
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
	 * 手机号码编辑框
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName="mnTextChanged")
	String mnTextChanged(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String mobileNum = (String) event.getProperty("changedText");
			mobileNumField.setValue(mobileNum);
		}
		return null;
	}

	
	/**
	 * 密码编辑框
	 * @param event
	 * @return
	 */
	@Command(commandName="pswTextChanged")
	String pswTextChanged(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String password = (String) event.getProperty("changedText");
			passwordField.setValue(password);
		}
		return null;
	}
	
	
	/**
	 * 找回密码
	 * @param event
	 * @return
	 */
	@Command(commandName="findPasswordBack")
	String findPasswordBack(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getWorkbench().showPage("userFindPswPage", null, null);
		}
		return null;
	}
	
	/**
	 * 快速注册
	 * @param event
	 * @return
	 */

	@Command(commandName="quickRegister")
	String quickRegister(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getWorkbench().showPage("userRegPage", null, null);
		}
		return null;
	}
	
}
