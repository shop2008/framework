package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.R.integer;

import com.wxxr.javax.ws.rs.core.NewCookie;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.android.ui.binding.GenericListAdapter;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.bean.User;
import com.wxxr.mobile.stock.client.bean.UserPic;

@View(name="user_pic_set")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_pic_set_layout")
public abstract class UserPicSetPage extends PageBase implements IModelUpdater {

	@Field(valueKey="options")
	List<UserPic> systemImages;
	
	
	
	DataField<List> systemImagesField;
	
	@Field
	String userIcon;

	
	/**
	 * 选中的位置
	 */
	@Field
	String selecedUserIcon = null;
	
	@OnShow
	protected void initData() {
		
		systemImages = new ArrayList<UserPic>();
		for(int i=0;i<6;i++) {
			String s = "resourceId:drawable/head"+(i+1);
			UserPic userPic = new UserPic();
			userPic.setImageURI(s);
			
			if (this.userIcon != null && this.userIcon.equals(s)) {
				userPic.setPicChecked(true);
			} else {
				userPic.setPicChecked(false);
			}
			systemImages.add(userPic);
		}
		systemImagesField.setValue(systemImages);
	}
	
	@Command(commandName="back")
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	
	@Command(commandName="selected")
	String selected(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			finish();
		}
		return null;
	}
	
	private void finish() {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
	}
	
	@OnHide
	public void hide() {
		if (selecedUserIcon != null) {
			this.setAttribute(AttributeKeys.text, ""+selecedUserIcon);
		}
	}
	@Override
	public void updateModel(Object value) {
		Map<String, String> map = (Map<String, String>)value;
		this.userIcon = map.get("curUserPic");
	}
	
	@Command(commandName="imageSelected")
	String imageSelected(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			UserPic userPic = this.systemImages.get(position);
			if (userPic.isPicChecked()) {
				userPic.setPicChecked(false);
				selecedUserIcon = null;
			} else {
				userPic.setPicChecked(true);
				selecedUserIcon = userPic.getImageURI();
			}
			
			for(int i=0;i<this.systemImages.size();i++) {
				
				if (i != position) {
					UserPic unSelectedItem = this.systemImages.get(i);
					unSelectedItem.setPicChecked(false);
				}
			}
			
		}
		return null;
	}
	
}
