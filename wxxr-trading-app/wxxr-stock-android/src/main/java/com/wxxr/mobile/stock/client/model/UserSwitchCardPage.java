package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.service.IUserManagementService;

@View(name="userSwitchCardPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.switch_bank_card_layout")
public abstract class UserSwitchCardPage extends PageBase implements IModelUpdater{

	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	@Field(valueKey="text")//,binding="123")
	String accountName;
	
	@Field(valueKey="text")
	String bankName;
	
	@Field(valueKey="text")
	String bankAddr;
	
	@Field(valueKey="text")
	String bankNum;
	
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
	@Command(commandName = "back", description = "Back To Last UI")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator()
					.hidePage(this);
		}
		return null;
	}
	
	/**
	 * 提交
	 * @param event
	 * @return
	 */
	@Command(commandName="commit")
	String commit(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			String accountNameStr = this.accountNameField.getValue();
			String bankNameStr = this.bankNameField.getValue();
			String bankAddrStr = this.bankAddrField.getValue();
			String bankNumStr = this.bankNumField.getValue();
			usrService.switchBankCard(accountNameStr, bankNameStr, bankAddrStr, bankNumStr);
		}
		return null;
	}
	
	String bankNameTextChanged(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String bankNameStr = (String)event.getProperty("changedText");
			this.bankName = bankNameStr;
			this.bankNameField.setValue(bankNameStr);
		}
		
		return null;
	}
	
	String bankAddrTextChanged(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String bankAddrStr = (String)event.getProperty("changedText");
			this.bankAddr = bankAddrStr;
			this.bankAddrField.setValue(bankAddrStr);
		}
		
		return null;
	}
	
	String bankNumTextChanged(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String bankNumStr = (String)event.getProperty("changedText");
			this.bankNum = bankNumStr;
			this.bankNumField.setValue(bankNumStr);
		}
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		Map<String, String> map = (Map<String, String>)value;
		String accountNameStr = map.get("accountName");
		this.accountName = accountNameStr;
		this.accountNameField.setValue(accountNameStr);
	}
}
