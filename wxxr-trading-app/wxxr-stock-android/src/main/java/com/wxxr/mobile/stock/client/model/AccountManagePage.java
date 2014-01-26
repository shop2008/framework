package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="AccountManagePage",withToolbar=true, description="我的帐号")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.account_manage_layout")
public abstract class AccountManagePage extends PageBase {

	@Menu(items={"left"})
	private IMenu toolbar;
	
	@Command(
			uiItems={
				@UIItem(id="left",label="返回",icon="resourceId:drawable/back_button_style")
			}
	)
	String toolbarClickedLeft(InputEvent event){
		hide();
		return null;
	}
	
	
	@Command(navigations={@Navigation(on="*", showPage="userAlterPswPage")})
	String enterAlterPasswrodPage(InputEvent event){
		//hide();
		return "*";
	}
	
	@Command
	String logout(InputEvent event){
		//hide();
		return "*";
	}
	
}
