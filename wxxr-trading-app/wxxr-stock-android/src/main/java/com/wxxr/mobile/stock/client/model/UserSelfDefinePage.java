package com.wxxr.mobile.stock.client.model;



import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
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
@View(name = "userSelfDefine")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.user_define_page_layout")
public abstract class UserSelfDefinePage extends PageBase {

	
	
	@Field(valueKey = "imageURI", binding = "${user!=null?user.userPic!=null?user.userPic:'resourceId:drawable/head1':'resourceId:drawable/head1'}")
	String userIcon;

	@Field(valueKey = "backgroundImageURI", binding = "${user!=null?user.homeBack!=null?user.homeBack:'resourceId:drawable/back1':'resourceId:drawable/back1'}")
	String userHomeBack;

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.myUserInfo}")
	UserBean user;

	@Command
	String back(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	@Command(commandName = "setPic", navigations = { @Navigation(on = "OK", showPage = "userPicSet") })
	String setPic(InputEvent event) {
		return "OK";
	}

/*	@Override
	public void updateModel(Object value) {

		Map<String, String> map = (Map<String, String>) value;
		if (map.containsKey("userPic")) {
			String userPic = map.get("userPic");
			curUsrPic = userPic;
			registerBean("userPic", userPic);
		}

		if (map.containsKey("userHomeBack")) {
			String userHomeBack = map.get("userHomeBack");
			curUsrHomeBack = userHomeBack;
			registerBean("userHomeBack", userHomeBack);
		}

		if (map.containsKey("selectPic")) {
			String userSelPic = map.get("selectPic");
			modifiedUPic = userSelPic;
			registerBean("userPic", userSelPic);
		}

		if (map.containsKey("selectHomePic")) {
			String userHomePic = map.get("selectHomePic");
			modifiedUHome = userHomePic;
			registerBean("userHomeBack", userHomePic);
		}
	}*/

/*	@Command(commandName = "setHomeBack", navigations = { @Navigation(on = "OK", showPage = "userHomeSet", params = { @Parameter(name = "add2BackStack", value = "false") }) })
	CommandResult setHomeBack(InputEvent event) {

		CommandResult result = null;
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			result = new CommandResult();
			Map<String, String> map = new HashMap<String, String>();
			map.put("userHomeBack", curUsrHomeBack);
			result.setPayload(map);
			result.setResult("OK");
		}
		return result;
	}*/
	
	@Command(commandName = "setHomeBack", navigations = { @Navigation(on = "OK", showPage = "userHomeSet") })
	String setHomeBack(InputEvent event) {
		return "OK";
	}
}
