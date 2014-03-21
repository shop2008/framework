package com.wxxr.mobile.stock.client.model;



import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

/**
 * 我的账号界面
 * 
 * @author renwenjie
 */
@View(name = "userSelfDefine" , withToolbar=true, description="个性化")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_define_page_layout")
public abstract class UserSelfDefinePage extends PageBase {

	
	
	@Field(valueKey = "imageURI", binding = "${user.userPic!=null?user.userPic:'resourceId:drawable/head4'}")
	String userIcon;

	@Field(valueKey = "backgroundImageURI", binding = "${user.homeBack!=null?user.homeBack:'resourceId:drawable/back1'}")
	String userHomeBack;

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;

	@Menu(items = { "left"})
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	@Command(commandName = "setPic", navigations = { @Navigation(on = "OK", showPage = "userPicSet") })
	String setPic(InputEvent event) {
		return "OK";
	}

	@Command(commandName = "setHomeBack", navigations = { @Navigation(on = "OK", showPage = "userHomeSet") })
	String setHomeBack(InputEvent event) {
		return "OK";
	}
}
