package com.wxxr.mobile.stock.client.model;

import android.R.bool;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.service.IUserManagementService;

/**
 * 用户账户
 * @author renwenjie
 *
 */
@View(name="userAccountPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_account_page_layout")
public abstract class UserAccountPage extends PageBase {

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
	
	
	@Field
	IUserManagementService userService;

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
			boolean isBindCard = userService.isBindCard();
			
			System.out.println("*****"+isBindCard);
			if (isBindCard) {
				/**进入提取现金界面*/
			} else {
				/**提示用户绑定银行卡*/
				showDialog();
			}
			
		}
		return null;
	}
	
	
	private void showDialog() {
		getUIContext().getWorkbenchManager().getWorkbench().showPage("unBindCardDialog", null, null);
	}

	@Command(commandName="incomeDetail")
	String incomeDetail(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 进入收支明细业务界面
		}
		return null;
	}
	
	@OnShow
	protected void initData() {
		userService = getUIContext().getKernelContext().getService(IUserManagementService.class);
	}
}
