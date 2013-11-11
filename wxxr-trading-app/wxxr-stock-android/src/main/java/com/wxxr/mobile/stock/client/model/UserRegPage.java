package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
@View(name = "userRegPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.quick_register_layout")
public abstract class UserRegPage extends PageBase {

	
	@Field(valueKey = "text")
	String mobileNum;
	DataField<String> mobileNumField;

	/**
	 * 是否阅读了《注册条款》
	 */
	@Field(valueKey="checked")
	boolean readChecked;
	
	DataField<Boolean> readCheckedField;
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
	 * 将密码发送到手机
	 * @param event
	 * @return
	 */
	@Command(commandName = "sendMsg")
	String sendMsg(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			// TODO 将密码发送到手机
			
		}
		return null;
	}
	
	
	/**
	 * 转向《注册规则》详细界面注册规则
	 * @param event
	 * @return
	 */
	@Command(commandName = "registerRules")
	String registerRules(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			// TODO 转向《注册规则》详细界面
			
		}
		return null;
	}
	@OnShow
	protected void initData() {
		this.readChecked = false;
		this.readCheckedField.setValue(false);
	}
}
