package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="myAccountPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_account_page_layout")
public abstract class MyAccountPage extends PageBase {

	/**
	 * 账户余额
	 */
	@Field(valueKey="text")
	String userBalance;
	
	/**
	 * 冻结资金
	 */
	@Field(valueKey="text")
	String userFreeze;
	
	
	/**
	 * 可用资金
	 */
	@Field(valueKey="text")
	String userAvalible;
	
	

	@Command(commandName="back")
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	@Command(commandName="drawCash")
	String drawCash(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 进入提取现金业务界面
		}
		return null;
	}
	
	@Command(commandName="incomeDetail")
	String incomeDetail(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 进入收支明细业务界面
		}
		return null;
	}
}
