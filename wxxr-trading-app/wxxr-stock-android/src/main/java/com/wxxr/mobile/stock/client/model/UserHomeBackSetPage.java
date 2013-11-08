package com.wxxr.mobile.stock.client.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.client.bean.UserPic;


@View(name="user_home_set")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_home_set_layout")
public abstract class UserHomeBackSetPage extends PageBase implements IModelUpdater {

	
	
	@Field(valueKey="options")
	List<UserPic> systemImages;
	
	
	
	DataField<List> systemImagesField;
	
	@Field
	String selectHomeBack;

	

	
	@OnShow
	protected void initData() {
		
		systemImages = new ArrayList<UserPic>();
		for(int i=0;i<4;i++) {
			String s = "resourceId:drawable/back"+(i+1);
			UserPic userPic = new UserPic();
			userPic.setImageURI(s);
			
			if (this.selectHomeBack != null && this.selectHomeBack.equals(s)) {
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
		if (selectHomeBack != null) {
			this.setAttribute(AttributeKeys.name, ""+selectHomeBack);
		}
	}
	@Override
	public void updateModel(Object value) {
		Map<String, String> map = (Map<String, String>)value;
		this.selectHomeBack = map.get("curHomeBack");
	}
	
	@Command(commandName="imageSelected")
	String imageSelected(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_ITEM_CLICK)) {
			int position = (Integer) event.getProperty("position");
			UserPic userPic = this.systemImages.get(position);
			if (userPic.isPicChecked()) {
				userPic.setPicChecked(false);
				selectHomeBack = null;
			} else {
				userPic.setPicChecked(true);
				selectHomeBack = userPic.getImageURI();
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
