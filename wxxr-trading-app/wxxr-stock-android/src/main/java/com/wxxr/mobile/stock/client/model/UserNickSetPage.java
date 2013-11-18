package com.wxxr.mobile.stock.client.model;

import android.text.TextUtils;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="userNickSet")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_nick_set_layout")
public abstract class UserNickSetPage extends PageBase {
	
	@Field(valueKey="text")
	String newNickName;
	
	DataField<String> newNickNameField;
	
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
	
	/**
	 * 更改昵称业务
	 * @param event
	 * @return
	 */
	@Command
	String done(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			String newNickName = this.newNickNameField.getValue();
			if (!TextUtils.isEmpty(newNickName) && this.user != null) {
				this.user.setNickName(newNickName);
			}
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	/**
	 * 当文本框输入的内容不为空的时候，会调用此函数
	 * @param event InputEvent.EVENT_TYPE_TEXT_CHANGED
	 * @return null
	 */
	@Command(commandName="newNickChanged")
	String newNickChanged(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String changedNick = (String)event.getProperty("changedText");
			this.newNickName = changedNick;
			this.newNickNameField.setValue(changedNick);
		}
		return null;
	}
}
