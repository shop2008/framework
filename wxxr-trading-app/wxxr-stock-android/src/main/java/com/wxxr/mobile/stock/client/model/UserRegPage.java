package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.model.UserRegCallback;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
@View(name = "userRegPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.quick_register_layout")
public abstract class UserRegPage extends PageBase {

	static Trace log = Trace.register(UserRegPage.class);
	@Field(valueKey = "text", binding="${callback.userName}")
	String mobileNum;
	
	@Field(
			valueKey="text", 
			enableWhen="${checked}", 
			attributes={@Attribute(name="enabled", value="${checked}")}
		    )
	String registerBtn;
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrService;

	
	@Bean
	UserRegCallback callback = new UserRegCallback();
	
	@Bean
	boolean checked = true;
	/**
	 * 是否阅读了《注册条款》
	 */
	@Field(valueKey="checked", binding="${checked}")
	boolean readChecked;
	
	/**
	 * 处理后退
	 * @param event
	 * @return
	 */
	@Command(commandName = "back")
	String back(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		}
		return null;
	}
	
	/**
	 * 将密码发送到手机
	 * @param event
	 * @return
	 */
	@Command(commandName = "sendMsg")
	@ExeGuard(title = "注册", message = "正在注册，请稍候...", silentPeriod = 1)
	String sendMsg(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			//将密码发送到手机
			if (log.isDebugEnabled()) {
				log.debug("register:Send Message To Mobile");
			}
			
			if (usrService != null) {
				usrService.register(this.callback.getUserName());
			}
		}
		return null;
	}
	
	
	/**
	 * 转向《注册规则》详细界面注册规则
	 * @param event
	 * @return
	 */
	@Command(
			commandName = "registerRules",
			navigations={@Navigation(on="OK", showPage="registerRulesPage")}
			)
	String registerRules(InputEvent event) {
		return "OK";
	}
	
	/**
	 * 设置CheckBox是否选中
	 * @param event InputEvent.EVENT_TYPE_CLICK
	 * @return null
	 */
	@Command(commandName="setReadChecked")
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
	}
}
