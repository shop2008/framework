package com.wxxr.mobile.stock.client.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.bean.UserPicBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.UserIcon;


@View(name="userHomeSet")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_home_set_layout")
public abstract class UserHomeBackSetPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;
	
	@Field(valueKey="options")
	List<UserIcon> systemImages;
	
	
	
	DataField<List> systemImagesField;
	
	String selectHomeBack = null;

	@OnShow
	protected void initData() {
		
		if(user != null) {
			String curHomeBack = user.getHomeBack();
			if (curHomeBack!=null) {
				selectHomeBack = curHomeBack;
			} else {
				selectHomeBack = "resourceId:drawable/back1";
			}
		} else {
			selectHomeBack = "resourceId:drawable/back1";
		}
		systemImages = new ArrayList<UserIcon>();
		for(int i=0;i<4;i++) {
			String s = "resourceId:drawable/back"+(i+1);
			UserIcon userIcon = new UserIcon();
			userIcon.setImageURI(s);
			
			if (this.selectHomeBack != null && this.selectHomeBack.equals(s)) {
				userIcon.setPicChecked(true);
			} else {
				userIcon.setPicChecked(false);
			}
			systemImages.add(userIcon);
		}
		systemImagesField.setValue(systemImages);
	}
	
	@Command
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	@Command
	String selected(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			if (user != null) {
				user.setHomeBack(selectHomeBack);
			}
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	@Command(commandName="imageSelected")
	String imageSelected(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			UserIcon userIcon = this.systemImages.get(position);
			if (userIcon.isPicChecked()) {
				userIcon.setPicChecked(false);
				selectHomeBack = null;
			} else {
				userIcon.setPicChecked(true);
				selectHomeBack = userIcon.getImageURI();
			}
			
			for(int i=0;i<this.systemImages.size();i++) {
				
				if (i != position) {
					UserIcon unSelectedItem = this.systemImages.get(i);
					unSelectedItem.setPicChecked(false);
				}
			}
			
		}
		return null;
	}
	
}
