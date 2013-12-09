package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.model.UserAlterPswCallback;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="userAlterPswPage",withToolbar=true, description="修改密码")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.alter_password_page_layout")
public abstract class UserAlterPswPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;
	
	@Field(valueKey="text", binding="${callback.oldPassword}")
	String oldPsw;
	
	@Field(valueKey="text", binding="${callback.newPassword}")
	String newPsw;
	
	@Field(valueKey="text", binding="${callback.newPasswordAgain}")
	String reNewPsw;
	
	@Bean
	UserAlterPswCallback callback = new UserAlterPswCallback();
	
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	

	/**
	 * 点击"确定"
	 * @param event
	 * @return
	 */
	@Command(commandName="done",navigations = { 
			@Navigation(
					on = "StockAppBizException", 
					message = "%m%n", 
					params = {
							@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
							@Parameter(name = "title", value = "错误")
							}
					) 
			}
	)
	@ExeGuard(title = "修改密码", message = "正在处理，请稍候...", silentPeriod = 1)
	String done(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			if (usrService != null) {
				usrService.updatePassword(this.callback.getOldPassword(), this.callback.getNewPassword(), this.callback.getNewPasswordAgain());
			}
			
			//hide();
		}
		return null;
	}
	
	@OnUIDestroy
	protected void clearData() {
		callback.setOldPassword("");
		callback.setNewPassword("");
		callback.setNewPasswordAgain("");
	}
}
