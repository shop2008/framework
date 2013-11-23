package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="userPicSet")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_pic_set_layout")
public abstract class UserPicSetPage extends PageBase {

	@Bean
	List<String> systemImageData;
	
	@Field(valueKey="options", binding="${systemImageData}")
	List<String> systemImages;
	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;
	
	@OnCreate
	protected void initData() {
		systemImageData = new ArrayList<String>();
		for(int i=0;i<6;i++) {
			String s = "resourceId:drawable/head"+(i+1);
			systemImageData.add(s);
		}
	}
	
	@Command
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	
	@Command(commandName="imageSelected")
	String imageSelected(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			String selPic = this.systemImageData.get(position);
			if (this.user != null) {
				this.user.setUserPic(selPic);
			}
		}
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	

	
}
