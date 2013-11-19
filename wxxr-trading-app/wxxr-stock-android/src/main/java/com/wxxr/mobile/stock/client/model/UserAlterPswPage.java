package com.wxxr.mobile.stock.client.model;

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

@View(name="userAlterPswPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.alter_password_page_layout")
public abstract class UserAlterPswPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;
	
	@Field(valueKey="text")
	String oldPsw;
	
	@Field(valueKey="text")
	String newPsw;
	
	@Field(valueKey="text")
	String reNewPsw;
	
	DataField<String> oldPswField;
	
	DataField<String> newPswField;
	
	DataField<String> reNewPswField;
	
	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "back", description = "Back To Last UI")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator()
					.hidePage(this);
		}
		return null;
	}

	
	
	@Command(commandName="reNewPswTextChanged")
	String reNewPswTextChanged(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String reNewPswStr = (String)event.getProperty("changedText");
			this.reNewPsw= reNewPswStr;
			this.reNewPswField.setValue(reNewPswStr);
		}
		return null;
	}
	
	@Command(commandName="newPswTextChanged")
	String newPswTextChanged(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String newPswStr = (String)event.getProperty("changedText");
			this.newPsw = newPswStr;
			this.newPswField.setValue(newPswStr);
		}
		return null;
	}
	
	
	@Command(commandName="oldPswTextChanged")
	String oldPswTextChanged(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String oldPswStr = (String)event.getProperty("changedText");
			this.oldPsw = oldPswStr;
			this.oldPswField.setValue(oldPswStr);
		}
		return null;
	}
	

	/**
	 * 点击"确定"
	 * @param event
	 * @return
	 */
	@Command(commandName="done")
	String done(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			String newPsw = this.newPswField.getValue();
			if (newPsw != null) {
				this.user.setPassword(newPsw);
			}
		}
		return null;
	}
}
