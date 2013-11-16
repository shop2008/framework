package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.biz.UserIcon;

@View(name="userPicSet")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_pic_set_layout")
public abstract class UserPicSetPage extends PageBase {

	@Field(valueKey="options")
	List<UserIcon> systemImages;
	
	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;
	
	DataField<List> systemImagesField;
	
	/**
	 * 选中的位置
	 */
	String selecedUserIcon = null;
	
	@OnShow
	protected void initData() {

		if(user != null) {
			String curUserPic = user.getUserPic();
			if (curUserPic!=null) {
				selecedUserIcon = curUserPic;
			} else {
				selecedUserIcon = "resourceId:drawable/head1";
			}
		} else {
			selecedUserIcon = "resourceId:drawable/head1";
		}
		
		systemImages = new ArrayList<UserIcon>();
		for(int i=0;i<6;i++) {
			String s = "resourceId:drawable/head"+(i+1);
			UserIcon userIcon = new UserIcon();
			userIcon.setImageURI(s);
			if (selecedUserIcon != null && selecedUserIcon.equals(s)) {
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
				user.setUserPic(selecedUserIcon);
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
				selecedUserIcon = null;
			} else {
				userIcon.setPicChecked(true);
				selecedUserIcon = userIcon.getImageURI();
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
