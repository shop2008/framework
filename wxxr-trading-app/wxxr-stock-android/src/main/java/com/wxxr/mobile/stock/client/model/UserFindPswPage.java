package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.service.IUserManagementService;


@View(name = "userFindPswPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.psw_find_back_layout")
public abstract class UserFindPswPage extends PageBase {
	
	@Field(valueKey = "text")
	String mobileNum;
	
	DataField<String> mobileNumField;
	
	@Bean(type=BindingType.Service)
	IUserManagementService userService;
	
	/**
	 * 处理后退
	 * @param event
	 * @return
	 */
	@Command(commandName = "back")
	String back(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 处理后退事件
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
	 * 发送密码到手机
	 * @param event
	 * @return
	 */
	@Command(commandName = "sendMsg")
	String sendMsg(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			if (userService != null) {
				
			}
		}
		return null;
	}
	
	


}
