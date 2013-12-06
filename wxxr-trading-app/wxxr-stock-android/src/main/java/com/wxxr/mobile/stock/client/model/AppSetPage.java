package com.wxxr.mobile.stock.client.model;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="appSetPage", withToolbar=true, description="设置")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.setting_page_layout")
public abstract class AppSetPage extends PageBase {

	@Field(valueKey="checked", binding="${usrService.pushMessageSetting}", visibleWhen="${user!=null?true:false}")
	boolean pushEnabled;
	
	@Field(valueKey="visible", binding="${user!=null?false:true}")
	boolean notLoginText;
	
	@Bean(type=BindingType.Service)
	IUserManagementService usrService;
	
	@Bean(type=BindingType.Pojo,express="${usrService.myUserInfo}")
	UserBean user;
	
	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button") })
	String toolbarClickedLeft(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
	
	/**
	 * 联系我们
	 * @param event
	 * @return
	 */
	@Command(commandName = "contractUs", 
			description = "Back To Last UI", 
			navigations={@Navigation(on="OK", showPage="constructUsPage")})
	String contractUs(InputEvent event) {
		return "OK";
	}

	/**
	 * 新手指引
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "useInstruction", description = "Back To Last UI",
			navigations={@Navigation(on="OK", showPage="guidePage")})
	String useInstruction(InputEvent event) {
		return "OK";
	}
	
	/**
	 * 给软件打分
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "playScore", description = "Back To Last UI")
	String playScore(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			//TODO 新手指引
		}
		return null;
	}
	
	/**
	 * 是否推送消息
	 * 
	 * @param event
	 * @return
	 */
	@Command(
			commandName = "setPushMsgEnabled", description = "Back To Last UI",
			navigations={@Navigation(on="*", showPage="userLoginPage"),
					@Navigation(on="StockAppBizException", message="%m%n", params={
							@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
							@Parameter(name = "title", value = "错误")
					})
			}
			)
	String setPushMsgEnabled(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			if (user == null) {
				return "*";
			}
			boolean pushEnabled = usrService.getPushMessageSetting();
			usrService.pushMessageSetting(!pushEnabled);
		}
		return null;
	}
	
}
