package com.wxxr.mobile.stock.client.model;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.model.UserNickSetCallBack;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="userNickSet")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_nick_set_layout")
public abstract class UserNickSetPage extends PageBase {
	
	
	@Field(valueKey="text", binding="${callBack.nickName}")
	String newNickName;
	
	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;
	
	
	@Bean
	UserNickSetCallBack callBack = new UserNickSetCallBack();
	
	
	@Command
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	/**
	 * 更改昵称业务
	 * @param event
	 * @return
	 */
	@Command(
			navigations = { 
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
	@ExeGuard(title = "修改昵称", message = "正在处理，请稍候...", silentPeriod = 1)
	String done(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			if (usrService != null) {
				usrService.updateNickName(callBack.getNickName());
				usrService.refreshUserInfo();
			}
			hide();
		}
		return null;
	}
	
	@OnUIDestroy
	protected void clearData() {
		callBack.setNickName("");
	}
}
