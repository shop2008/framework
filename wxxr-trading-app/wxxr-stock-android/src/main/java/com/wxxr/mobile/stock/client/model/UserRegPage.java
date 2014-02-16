package com.wxxr.mobile.stock.client.model;


import android.os.SystemClock;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.model.UserRegCallback;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;

@View(name = "userRegPage", withToolbar = true, description = "快速注册")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.quick_register_layout")
public abstract class UserRegPage extends PageBase {

	static Trace log = Trace.register(UserRegPage.class);
	@Field(valueKey = "text", binding = "${callback.userName}")
	String mobileNum;

	@Field(valueKey = "text", binding = "${callback.password}")
	String newPassword;

	@Field(valueKey = "text", binding = "${callback.retypePassword}")
	String reNewPassword;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	@Field(valueKey = "text", enableWhen = "${checked}")
	String registerBtn;

	@Bean(type = BindingType.Service)
	IUserLoginManagementService usrService;

	@Bean
	UserRegCallback callback = new UserRegCallback();

	@Bean
	boolean checked = true;
	/**
	 * 是否阅读了《注册条款》
	 */
	@Field(valueKey = "checked", binding = "${checked}")
	boolean readChecked;

	@Command(commandName = "commit", navigations = { @Navigation(on = "StockAppBizException", message = "%m%n", params = {
			@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "提示") }),
			@Navigation(on="OK", showPage="userNickSet")
	})
	@NetworkConstraint
	@ExeGuard(title = "注册", message = "正在注册，请稍候...", silentPeriod = 200)
	String commit(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {

			SystemClock.sleep(500);
			if (usrService != null) {
				usrService.register(this.callback.getUserName(),
						this.callback.getPassword(),
						this.callback.getRetypePassword());
			}
			
			
			hide();
			return "OK";
		}
		return null;
	}

	/**
	 * 转向《注册规则》详细界面注册规则
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "registerRules", navigations = { @Navigation(on = "OK", showPage = "registerRulesPage") })
	String registerRules(InputEvent event) {
		return "OK";
	}

	/**
	 * 设置CheckBox是否选中
	 * 
	 * @param event
	 *            InputEvent.EVENT_TYPE_CLICK
	 * @return null
	 */
	@Command(commandName = "setReadChecked")
	String setReadChecked(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			checked = !checked;
			registerBean("checked", checked);
		}
		return null;
	}

	@OnUIDestroy
	protected void clearData() {
		callback.setUserName("");
		callback.setPassword("");
		callback.setRetypePassword("");
	}
}
