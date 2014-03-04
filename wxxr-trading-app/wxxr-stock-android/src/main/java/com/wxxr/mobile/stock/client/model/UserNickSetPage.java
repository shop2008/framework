package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.BeanValidation;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.FieldUpdating;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.common.AsyncUtils;
import com.wxxr.mobile.stock.app.model.UserNickSetCallBack;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "userNickSet", withToolbar = true, description="设置昵称")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_nick_set_layout")
public abstract class UserNickSetPage extends PageBase {

	@Field(valueKey = "text", binding = "${callBack.nickName}")
	String newNickName;
	DataField<String> newNickNameField;
	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;

	@Bean
	UserNickSetCallBack callBack = new UserNickSetCallBack();

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Field(valueKey = "enabled")//, enableWhen = "${newNickNameField.getValidationErrors()==null}")
	boolean setNick = true;

	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	/**
	 * 更改昵称业务
	 * 
	 * @param event
	 * @return
	 */
	@Command(
			updateFields = {
				@FieldUpdating(fields={"newNickName"},message="请输入正确的昵称")
			},
			validations = { @BeanValidation(bean = "callBack", message = "请输入正确的昵称") }, navigations = {
			@Navigation(on = "ValidationException", message = "%m%n", params = {
					@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
					@Parameter(name = "title", value = "输入验证错误") }),
			@Navigation(on = "StockAppBizException", message = "%m%n", params = {
					@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
					@Parameter(name = "title", value = "错误") }) })
	@ExeGuard(title = "修改昵称", message = "正在处理，请稍候...", silentPeriod = 1)
	String commit(ExecutionStep step, InputEvent event, Object result) {
		switch(step){
		case PROCESS:
			if (usrService != null) {
				AsyncUtils.execRunnableAsyncInUI(new Runnable() {
					
					@Override
					public void run() {
						usrService.updateNickName(callBack.getNickName());
						usrService.refreshUserInfo();
					}
				});
			}
			break;
		case NAVIGATION:
			hide();
			break;
		}
		return null;

	}

	/**清空文本框输入内容*/
	@Command
	String clearTextContent(InputEvent event) {
		//callBack.setNickName(nickName)
		newNickNameField.setValue("");
		this.callBack.setNickName("");
		return null;
	}
	
	
	@OnUIDestroy
	protected void clearData() {
		callBack.setNickName("");
	}
	
	
}
