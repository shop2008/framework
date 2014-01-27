package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="PushMessageSetPage",withToolbar=true, description="推送设置")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.push_mesage_setting")
public abstract class PushMessageSetPage extends PageBase {

	@Field(valueKey="checked")
	boolean pushMsgEnabled;
	
	@Menu(items = {"left"})
	protected IMenu toolbar;
	
	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Command
	String pushMsgStatusChanged(InputEvent event) {
		
		Boolean isChecked = (Boolean) event.getProperty("isChecked");
		
		if(isChecked != null)
			System.out.println("isChecked--->"+isChecked);
		return null;
	}
}
