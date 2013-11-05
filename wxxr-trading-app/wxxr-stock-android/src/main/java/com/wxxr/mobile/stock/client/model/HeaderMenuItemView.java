/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;

/**
 * @author neillin
 *
 */
@View(name="headerMenuItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.layout_right_navi_content")
public abstract class HeaderMenuItemView extends ViewBase implements IModelUpdater{

	@Field(valueKey="imageURI")
	String headIcon;
	
	@Field(valueKey="text")
	String nickName;
	
	@Field(valueKey="text")
	String userNum;
	
	@Field(valueKey="text")
	String unreadNews;
	
	@Field(valueKey="text")
	String integralBalance;
	
	@Field(valueKey="text")
	String accountBalance;
	
	DataField<String> headIconField;
	DataField<String> nickNameField;
	DataField<String> userNumField;
	DataField<String> unreadNewsField;
	DataField<String> integralBalanceField;
	DataField<String> accountBalanceField;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IModelUpdater#updateModel(java.lang.Object)
	 */
	@Override
	public void updateModel(Object val) {
		if(val instanceof IUICommand){
			IUICommand cmd = (IUICommand)val;
			String value = cmd.getAttribute(AttributeKeys.name);
			setNickName(value);
			value = cmd.getAttribute(AttributeKeys.title);
			setUserNum(value);
			if(cmd.hasAttribute(AttributeKeys.visible)){
				setAttribute(AttributeKeys.visible, cmd.getAttribute(AttributeKeys.visible));
			}
			if(cmd.hasAttribute(AttributeKeys.enabled)){
				setAttribute(AttributeKeys.enabled, cmd.getAttribute(AttributeKeys.enabled));
			}
		}
		
	}

	public String getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
		this.headIconField.setValue(headIcon);
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
		this.nickNameField.setValue(nickName);
	}

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
		this.userNumField.setValue(userNum);
	}

	public String getUnreadNews() {
		return unreadNews;
	}

	public void setUnreadNews(String unreadNews) {
		this.unreadNews = unreadNews;
		this.unreadNewsField.setValue(unreadNews);
	}

	public String getIntegralBalance() {
		return integralBalance;
	}

	public void setIntegralBalance(String integralBalance) {
		this.integralBalance = integralBalance;
		this.integralBalanceField.setValue(integralBalance);
	}

	public String getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(String accountBalance) {
		this.accountBalance = accountBalance;
		this.accountBalanceField.setValue(accountBalance);
	}
}
