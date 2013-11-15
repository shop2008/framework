package com.wxxr.mobile.stock.client.model;


import javax.security.auth.login.LoginException;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.rpc.rest.IsHttpMethod;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="appSetPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.setting_page_layout")
public abstract class AppSetPage extends PageBase {

	@Field(valueKey="checked", visibleWhen="${userService.isLogin?true:false}", attributes={@Attribute(name="checked", value="${isChecked==true?true:false}")})
	boolean pushEnabled;
	
	DataField<Boolean> pushEnabledField;
	
	
	@Field
	IUserManagementService userService;
	
	
	@Field(valueKey="text", visibleWhen="${userService.isLogin?false:true}")
	String notLoginText;
	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "back", description = "Back To Last UI")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
		
		}
		return null;
	}

	boolean isChecked = userService.getPushMessageSetting();;
	
	@OnCreate
	protected void initData() {
		
		registerBean("isChecked", isChecked);
	}
	
	/**
	 * 联系我们
	 * @param event
	 * @return
	 */
	@Command(commandName = "contractUs", description = "Back To Last UI")
	String contractUs(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 联系我们
		}
		return null;
	}

	/**
	 * 新手指引
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "useInstruction", description = "Back To Last UI")
	String useInstruction(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 新手指引
		}
		return null;
	}
	
	/**
	 * 给软件打分
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "playScore", description = "Back To Last UI")
	String playScore(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 新手指引
		}
		return null;
	}
	
	/**
	 * 提示用户登录
	 * @param event
	 * @return
	 * @throws LoginException 
	 */
	@Command(commandName = "notLogin", description = "Alert User Login!")
	String notLogin(InputEvent event) throws LoginException {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 未登录，提示登录
			throw new LoginException();
		}
		return null;
	}
	
	/**
	 * 是否推送消息
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "setPushMsgEnabled", description = "Back To Last UI")
	String setPushMsgEnabled(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			isChecked = !isChecked;
			this.userService.pushMessageSetting(isChecked);
		}
		return null;
	}

}
