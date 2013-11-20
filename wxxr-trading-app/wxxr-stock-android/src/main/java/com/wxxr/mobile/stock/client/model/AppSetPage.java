package com.wxxr.mobile.stock.client.model;


import android.R.bool;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="appSetPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.setting_page_layout")
public abstract class AppSetPage extends PageBase {

	@Field(valueKey="checked", binding="${user!=null?user.messagePushSettingOn==true?true:false:false}",visibleWhen="${user!=null?user.login==true?true:false:false}")
	boolean pushEnabled;
	
	@Field(valueKey="visible", binding="${user!=null?user.login==true?false:true:true}")
	boolean notLoginText;
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type=BindingType.Pojo,express="${usrService!=null?usrService.myUserInfo:null}")
	UserBean user;
	
	
	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "back", description = "Back To Last UI")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
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
	 * 是否推送消息
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "setPushMsgEnabled", description = "Back To Last UI")
	String setPushMsgEnabled(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			if (this.user != null) {
				boolean isPushMessage = this.user.getMessagePushSettingOn();
				if (isPushMessage) {
					this.user.setMessagePushSettingOn(false);
				} else {
					this.user.setMessagePushSettingOn(true);
				}
			}
		}
		return null;
	}
	
	@Command(
			commandName="notLogin",
			navigations={@Navigation(on="OK", showPage="userLoginPage")}
			)
	String notLogin(InputEvent event) {
		return "OK";
	}
}
