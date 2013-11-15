package com.wxxr.mobile.stock.client.model;

import java.util.List;

import android.R.bool;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDataChanged;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.AccountInfoBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

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
	AccountInfoBean bean;
	
	@Bean(type=BindingType.Service)
	IUserManagementService userService;


	DataField<String> userBalanceField;
	DataField<String> userFreezeField;
	DataField<String> userAvalibleField;
	
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
	
	@OnCreate
	void injectServices() {
		this.userService = AppUtils.getService(IUserManagementService.class);
	}
	
	private void showDialog() {
		getUIContext().getWorkbenchManager().getWorkbench().showPage("unBindCardDialog", null, null);
	}

	/**
	 * 进入收支明细业务界面
	 * @param event
	 * @return
	 */
	@Command(
			commandName="incomeDetail",
			navigations={
					@Navigation(on="OK", 
					showPage="uRealPanelScorePage", 
					params={@Parameter(name="incomeDetail",value="{$entity.}")})
			}
	)
	String incomeDetail(InputEvent event) {
		
		
		return "OK";
	}
	
	/**
	 * 进入实盘积分明细
	 * @param event
	 * @return
	 */
	@Command(
			commandName="actualIntegralDetail",
			navigations={
					@Navigation(on="OK", 
					showPage="uRealPanelScorePage", 
					params={@Parameter(name="scores",value="{$entity.}")})
			}
	)
	String actualIntegralDetail(InputEvent event) {
		
		
		return "OK";
	}
	
	@OnShow
	protected void initData() {
		userService = getUIContext().getKernelContext().getService(IUserManagementService.class);
		//bean = userService.fetchUserAccountInfo("");
	}
	
	@OnDataChanged
	protected void dataChanged(ValueChangedEvent event) {
		if (bean != null) {
			this.userAvalible = bean.getAvalible();
			this.userAvalibleField.setValue(bean.getAvalible());
			
			this.userBalance = bean.getBalance();
			this.userBalanceField.setValue(bean.getBalance());
			
			this.userFreeze = bean.getFreeze();
			this.userFreezeField.setValue(bean.getFreeze());
		}
	}
}
