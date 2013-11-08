/**
 * 
 */
package com.wxxr.mobile.stock.client.model;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;

/**
 * @author neillin
 *
 */
@View(name="myAuthPage")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.user_token_page_layout")
public abstract class MyAuthPage extends PageBase {

	
	/**
	 * 认证手机号
	 */
	@Field(valueKey="text")
	String authMobileNum;
	
	
	/**
	 * 未认证
	 */
	@Field(valueKey="text")
	String bankNotAuth;
	
	/**
	 * 银行卡认证，用于控制已认证布局的显示及隐藏
	 */
	@Field(valueKey="visible")
	boolean authedBodyVisible;
	
	
	/**
	 * 银行卡认证，用于控制未认证布局的显示及隐藏
	 */
	@Field(valueKey="visible")
	boolean notAuthBodyVisible;
	
	
	/**
	 * 银行卡用户名
	 */
	
	@Field(valueKey="text")
	String accountName;
	
	/**
	 * 开户银行名称
	 */
	@Field(valueKey="text")
	String bankName;
	
	/**
	 * 银行卡号
	 */
	@Field(valueKey="text")
	String bankNum;
	
	
	DataField<String> authMobileNumField;
	DataField<String> bankNotAuthField;
	DataField<Boolean> authedBodyVisibleField;
	DataField<Boolean> notAuthBodyVisibleField;
	DataField<String> accountNameField;
	DataField<String> bankNameField;
	DataField<String> bankNumField;
	/**
	 * 银行卡认证
	 * @param event
	 * @return
	 */
	@Command(commandName="bankAuth")
	String bankAuth(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 银行卡认证
		}
		
		return null;
	}
	
	@OnShow
	protected void initData() {
		this.notAuthBodyVisible = true;
		this.authedBodyVisible = false;
		this.notAuthBodyVisibleField.setValue(true);
		this.authedBodyVisibleField.setValue(false);
	}
	
	/**
	 * 更换银行卡
	 * @param event
	 * @return
	 */
	@Command(commandName="switchBankCard")
	String switchBankCard(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 更换银行卡
		}
		
		return null;
	}
	
	/**
	 * 返回到上一界面
	 * @param event
	 * @return
	 */
	@Command(commandName="back")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
}
