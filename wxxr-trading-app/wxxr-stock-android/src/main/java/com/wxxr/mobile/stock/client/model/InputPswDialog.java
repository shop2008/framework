package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.model.AuthInfo;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "InputPswDialog")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.input_password_dialog")
public abstract class InputPswDialog extends ViewBase implements IModelUpdater {

	@Field(valueKey = "text")
	String inputPsw;

	DataField<String> inputPswField;

	@Field(valueKey = "text")
	String commitVerify;

	String type;
	String moneyAmount;
	@Bean(type = BindingType.Service)
	IUserManagementService userService;

	@Bean(type = BindingType.Pojo, express = "${userService.myUserInfo}")
	UserBean user;
	
	@Bean(type = BindingType.Service)
	ITradingManagementService tradingService;

	@Bean(type = BindingType.Pojo, express = "${userService.userAuthInfo}")
	AuthInfo authBean;

	@Command(navigations = {
			@Navigation(on = "VerifyFail", showDialog = "RetryVerifyDialog", closeCurrentView = true),
			@Navigation(on = "DRAWCASH", showPage = "userWithDrawCashPage", closeCurrentView = true),
			@Navigation(on = "SWITCHCARD", showPage = "userSwitchCardPage", closeCurrentView = true),
			@Navigation(on = "UnConfirmDialogView", showDialog = "UnConfirmDialogView", closeCurrentView = true),
			@Navigation(on = "StockAppBizException", message = "%m%n", params = {
					@Parameter(name = "autoClosed", type =
					ValueType.INETGER, value = "2"),
			
					@Parameter(name = "title", value = "错误") }),
			@Navigation(on="WaitVerify", showDialog="WaitVerifyDialogView")
			})
	@ExeGuard(cancellable=true,silentPeriod=200,title="提示",message="正在处理，请稍候...")
	CommandResult commitVerifyPasswd(InputEvent event) {
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String inputPasswrod = this.inputPswField.getValue();
		boolean verifyResult = userService.verfiy(user.getPhoneNumber(),
				inputPasswrod);
		CommandResult result = new CommandResult();
		if (verifyResult) {
			
			if (type.equals("switchBankCard")) {
				hide();
				result.setResult("SWITCHCARD");
				return result;
			} else if (type.equals("UserAccountPage")) {
				hide();
				result.setResult("DRAWCASH");
				return result;
			} else if (type.equals("WithDrawCash")) {
				if (authBean != null) {
					boolean confirmed = authBean.getConfirmed();
					if (confirmed) {
						hide();
						tradingService.applyDrawMoney(Long.parseLong(moneyAmount)*100);
						result.setResult("WaitVerify");
						return result;
					} else {
						hide();
						Map<String, String> map = new HashMap<String, String>();
						map.put("moneyAmount", moneyAmount);
						result.setPayload(map);
						result.setResult("UnConfirmDialogView");
						return result;
					}
				}
				return null;
			}
		} else {
			hide();
			result.setResult("VerifyFail");
			return result;
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
				
				if(tempt != null && "moneyAmount".equals("key")) {
					if (tempt instanceof String) {
						moneyAmount = (String) tempt;
					}
				}
			}
		}

	}
}
