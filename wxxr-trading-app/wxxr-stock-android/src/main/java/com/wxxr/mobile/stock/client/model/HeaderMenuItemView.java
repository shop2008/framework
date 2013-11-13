/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.UserInfoEntity;
import com.wxxr.mobile.stock.client.service.IUserManagementService;

/**
 * @author neillin
 *
 */
@View(name="headerMenuItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.layout_right_navi_content")
public abstract class HeaderMenuItemView extends ViewBase {
	private static Trace log;
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrMgr;

	@Bean(type=BindingType.Pojo)
	UserInfoEntity userInfo;
	
	@Field(valueKey="visible")
	boolean userRegistered;
	
	DataField<Boolean> userRegisteredField;
	
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
	String handleClickImage(InputEvent event) {
		log.info("User click on user image !");
		getUIContext()
				.getWorkbenchManager()
				.getPageNavigator()
				.showPage(
						getUIContext().getWorkbenchManager().getWorkbench()
								.getPage("userLoginPage"), null, null);
		return null;
	}
	
	@Command
	String handleClickBalance(InputEvent event){
		log.info("User click on Account balance !");
		return null;
	}
	@Command
	String handleClickUnread(InputEvent event){
		log.info("User click on Unread acticles !");
		return null;
	}

	@Command
	String handleClickCash(InputEvent event){
		log.info("User click on cash icon !");
		return null;
	}

	@OnCreate
	void injectServices() {
		this.usrMgr = AppUtils.getService(IUserManagementService.class);
	}
	
	@OnShow
	private void updateRightMenu() {
		this.userInfo = this.usrMgr.getMyInfo();
		
		if(this.userInfo == null){
			this.userRegistered = false;
			this.userRegisteredField.setValue(false);
			this.nickName = "登录账号";
			this.nickNameField.setValue(this.nickName);
			this.userNum = "赶快登录赚实盘积分吧";
			this.userNumField.setValue(this.userNum);
		}else{
			this.userRegistered = true;
			this.userRegisteredField.setValue(true);
			this.nickName = userInfo.getNickName();
			this.nickNameField.setValue(this.nickName);
			this.userNum = userInfo.getPhoneNumber();
			this.userNumField.setValue(this.userNum);
		}
	}

	

}
