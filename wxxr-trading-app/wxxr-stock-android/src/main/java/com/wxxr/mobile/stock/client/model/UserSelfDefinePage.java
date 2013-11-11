package com.wxxr.mobile.stock.client.model;


import java.util.HashMap;
import java.util.Map;

import android.widget.GridView;
import android.widget.TabHost;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnDataChanged;
import com.wxxr.mobile.core.ui.annotation.OnDestroy;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageCallback;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValueChangedEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;


/**
 * 我的账号界面
 * @author renwenjie
 */
@View(name="userSelfDefine")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.user_define_page_layout")
public abstract class UserSelfDefinePage extends PageBase implements IModelUpdater {

	@Field(valueKey="imageURI")
	String userHomeBack;
	
	@Field(valueKey="imageURI")
	String userIcon;
	
	@Field
	String selectedPic = null;
	
	@Field
	String selectedHomeBack = null;
	
	DataField<String> userHomeBackField;
	DataField<String> userIconField;
	
	
	@OnShow
	protected void fun1() {
		if (selectedPic != null) {
			this.userIcon = selectedPic;
			this.userIconField.setValue(selectedPic);
			
			
		}
		
		if (selectedHomeBack != null) {
			this.userHomeBack = selectedHomeBack;
			this.userHomeBackField.setValue(selectedHomeBack);
		}
	}
	
	
	@Command(commandName="back")
	String back(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	
	@Command(commandName="setPic")
	String setPic(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("curUserPic", this.userIconField.getValue());
			getUIContext().getWorkbenchManager().getWorkbench().showPage("user_pic_set", map, new IPageCallback() {
				
				@Override
				public void onShow(IPage page) {
				
				}
				
				@Override
				public void onHide(IPage page) {
					selectedPic = page.getAttribute(AttributeKeys.text);
					userIcon = selectedPic;
					userIconField.setValue(selectedPic);
				}
				
				@Override
				public void onDestroy(IPage page) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onCreate(IPage page) {
					// TODO Auto-generated method stub
				}
			});
		}
		return null;
	}

	
	
	
	
	@Override
	public void updateModel(Object value) {
		System.out.println("---updateModel---");
		Map<String, String> map = (Map<String, String>)value;
		if (map != null) {
			this.userIcon = map.get("curUserIcon");
			this.userIconField.setValue(map.get("curUserIcon"));
			this.userHomeBack = map.get("curUserHomeBack");
			this.userHomeBackField.setValue(map.get("curUserHomeBack"));
		}
		
	}
	
	@Command(commandName="setHomeBack")
	String setHomeBack(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("curHomeBack", this.userHomeBackField.getValue());
			getUIContext().getWorkbenchManager().getWorkbench().showPage("user_home_set", map, new IPageCallback() {
				
				@Override
				public void onShow(IPage page) {
				
				}
				
				@Override
				public void onHide(IPage page) {
					selectedHomeBack = page.getAttribute(AttributeKeys.name);
					userHomeBack = selectedHomeBack;
					userHomeBackField.setValue(selectedHomeBack);
				}
				
				@Override
				public void onDestroy(IPage page) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onCreate(IPage page) {
					// TODO Auto-generated method stub
				}
			});
		}
		return null;
	}
	
	@OnHide
	public void hide() {
		
		//setAttribute(AttributeKeys.text, selectedHomeBack);
		setAttribute(AttributeKeys.text, selectedPic);
		setAttribute(AttributeKeys.name, selectedHomeBack);
		
		selectedPic = null;
		selectedHomeBack = null;
	}
	
}
