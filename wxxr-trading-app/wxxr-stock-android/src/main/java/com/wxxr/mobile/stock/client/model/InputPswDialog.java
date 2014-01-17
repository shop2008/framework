package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.javax.ws.rs.BeanParam;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "InputPswDialog")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.input_password_dialog")
public abstract class InputPswDialog extends ViewBase implements IModelUpdater {

	@Field(valueKey = "text")
	String inputPsw;

	DataField<String> inputPswField;

	String type;
	@Bean(type = BindingType.Service)
	IUserManagementService userService;

	@Bean(type = BindingType.Pojo, express = "${userService.myUserInfo}")
	UserBean user;

	@Command(navigations = {
			@Navigation(on = "VerifyFail", showDialog = "RetryVerifyDialog", closeCurrentView = true),
			@Navigation(on = "TODRAWCASH", showPage = "userWithDrawCashPage"),
			@Navigation(on = "TOSWITCHCARD", showPage = "userSwitchCardPage") })
	String commitVerify(InputEvent event) {
		String inputPasswrod = this.inputPswField.getValue();
		boolean verifyResult = userService.verfiy(user.getPhoneNumber(),
				inputPasswrod);
		System.out.println("verifyResult----"+verifyResult);
		if (verifyResult) {
			if (type.equals("switchBankCard")) {
				return "TOSWITCHCARD";
			} else if (type.equals("UserAccountPage")) {
				return "TODRAWCASH";
			}
		} else {
			return "VerifyFail";
		}
		return null;
	}

	@Command
	String cancel(InputEvent event) {
		hide();
		return null;
	}

	@Command
	String fetchChangeText(InputEvent event) {
		String typePassword = (String) event.getProperty("changedText");
		this.inputPswField.setValue(typePassword);
		return null;
	}

	@Override
	public void updateModel(Object value) {
		if (value instanceof Map) {
			Map temp = (Map) value;
			for (Object key : temp.keySet()) {
				Object tempt = temp.get(key);
				if (tempt != null && "type".equals(key)) {
					if (tempt instanceof String) {
						type = (String) tempt;
					}
				}
			}
		}

	}
}
