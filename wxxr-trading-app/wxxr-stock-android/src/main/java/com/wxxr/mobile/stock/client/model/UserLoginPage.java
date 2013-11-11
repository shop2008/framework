package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name = "userLoginPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.login_layout")
public abstract class UserLoginPage extends PageBase {

	@Field(valueKey = "text")
	String mobileNum;

	@Field(valueKey = "text")
	String password;
	
	@Field(valueKey = "text")
	String loginBtn;

	DataField<String> mobileNumField;

	DataField<String> passwordField;
	
	DataField<String> loginBtnField;

	/**
	 * 处理登录
	 * @param event
	 * @return
	 */
	@Command(commandName = "login")
	String login(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			String mobileNum = mobileNumField.getValue();
			String password = passwordField.getValue();
			// TODO 处理登录
			
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
			//TODO 处理后退事件
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
			//TODO 找回密码
		}
		return null;
	}
	
	/**
	 * 找回密码
	 * @param event
	 * @return
	 */
	@Command(commandName="quickRegister")
	String quickRegister(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 快速注册
		}
		return null;
	}
	
}
