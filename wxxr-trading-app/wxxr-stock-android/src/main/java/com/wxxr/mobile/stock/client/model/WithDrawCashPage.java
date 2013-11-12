package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;


/**
 * 提现认证
 * @author renwenjie
 */
@View(name="withDrawCashPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.withdraw_cash_auth_layout")
public abstract class WithDrawCashPage extends PageBase {

	
	/**
	 * 用户名
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
	
	
	DataField<String> accountNameField;
	DataField<String> bankNameField;
	DataField<String> bankAddrField;
	DataField<String> bankNumField;
	/**
	 * 返回到上一界面
	 * @param event 点击事件
	 * @return null 
	 */
	@Command(commandName="back")
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			//TODO 返回到上一界面
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}

	/**
	 * 提现认证
	 * @param event 点击事件
	 * @return null 
	 */
	@Command(commandName="cashAuth")
	String cashAuth(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			//TODO 提现认证
		}
		return null;
	
	}
}
