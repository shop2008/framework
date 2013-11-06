/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnDataChanged;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;

/**
 * @author neillin
 *
 */
@View(name="headerMenuItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.layout_right_navi_content")
public abstract class HeaderMenuItemView extends ViewBase {
	private static Trace log;
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

	@Command(commandName="handleClickImage")
	private String handleClickImage(InputEvent event){
		log.info("User click on user image !");
		return null;
	}
	
	@Command
	private String handleClickBalance(InputEvent event){
		log.info("User click on Account balance !");
		return null;
	}
	@Command
	private String handleClickUnread(InputEvent event){
		log.info("User click on Unread acticles !");
		return null;
	}

	@Command
	private String handleClickCash(InputEvent event){
		log.info("User click on cash icon !");
		return null;
	}

	
	public String getHeadIcon() {
		return headIcon;
	}
	
	@OnDataChanged
	private void updateHeaderStatus(ValueChangedEvent event){
		if(event.getComponent() == this.userNumField){
		if(this.userNum == null){
			this.headIconField.setAttribute(AttributeKeys.visible, false);
		}else{
			this.headIconField.setAttribute(AttributeKeys.visible, true);
			
		}
		}
	}

	@OnShow
	private void updateHeaderStatus(IBinding<IView> binding){
		if(this.userNum == null){
			this.headIconField.setAttribute(AttributeKeys.visible, false);
		}else{
			this.headIconField.setAttribute(AttributeKeys.visible, true);
			
		}
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
