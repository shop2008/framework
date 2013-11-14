package com.wxxr.mobile.stock.client.model;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.service.IUserManagementService;

@View(name="withDrawCashAuthPage")
@AndroidBinding(type=AndroidBindingType.ACTIVITY, layoutId="R.layout.withdraw_cash_auth_layout")
public abstract class WithDrawCashAuthPage extends PageBase {

	
	/**
	 * 户名
	 */
	@Field(valueKey="text")
	String accountName;
	
	/**
	 * 银行名称
	 */
	@Field(valueKey="text")
	String bankName;
	
	/**
	 * 开户行所在地
	 */
	@Field(valueKey="text")
	String bankAddr;
	
	/**
	 * 银行卡号
	 */
	@Field(valueKey="text")
	String bankNum;
	
	@Field
	IUserManagementService userService;
	
	DataField<String> accountNameField;
	
	DataField<String> bankNameField;
	
	DataField<String> bankAddrField;
	
	DataField<String> bankNumField;
	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(
			commandName = "back"
			)
	String back(InputEvent event) {
		
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	
	
	@Command(commandName="accountNameChanged")
	String accountNameChanged(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String accountName = (String)event.getProperty("changedText");
			this.accountName = accountName;
			this.accountNameField.setValue(accountName);
		}
		return null;
	}
	
	@Command(commandName="bankNameChanged")
	String bankNameChanged(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String bankName = (String)event.getProperty("changedText");
			this.bankName = bankName;
			this.bankNameField.setValue(bankName);
		}
		return null;
	}
	
	@Command(commandName="bankAddrChanged")
	String bankAddrChanged(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String bankAddr = (String)event.getProperty("changedText");
			this.bankAddr = bankAddr;
			this.bankAddrField.setValue(bankAddr);
		}
		return null;
	}
	
	@Command(commandName="bankNumChanged")
	String bankNumChanged(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String bankNum = (String)event.getProperty("changedText");
			this.bankNum = bankNum;
			this.bankNumField.setValue(bankNum);
		}
		return null;
	}

	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "cashAuth", description = "Back To Last UI")
	String cashAuth(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			userService.bindBankCard(accountNameField.getValue(), bankNameField.getValue(), bankAddrField.getValue(), bankNameField.getValue());
		}
		return null;
	}
	
	@OnShow
	protected void initData() {
		userService = getUIContext().getKernelContext().getService(IUserManagementService.class);
	}
}
