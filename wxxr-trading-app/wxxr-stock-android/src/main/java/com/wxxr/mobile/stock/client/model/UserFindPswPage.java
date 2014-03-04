package com.wxxr.mobile.stock.client.model;

import java.util.Timer;
import java.util.TimerTask;

import android.os.SystemClock;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.FieldUpdating;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUICreate;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.model.UserFindPswCallBack;
import com.wxxr.mobile.stock.app.service.IUserLoginManagementService;
import com.wxxr.mobile.stock.client.utils.Utils;

@View(name = "userFindPswPage", withToolbar = true, description = "找回密码")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.psw_find_back_layout")
public abstract class UserFindPswPage extends PageBase {

	@Field(valueKey = "text", binding = "${callBack.phoneNum}")
	String mobileNum;
	DataField<String> mobileNumField;

	@Bean(type = BindingType.Service)
	IUserLoginManagementService userService;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Field(valueKey="text")
	String findPasswordBtn;
	DataField<String> findPasswordBtnField;
	
	
	private Timer  timer;
	
	private TimerTask task;
	
	private int totalSeconds; 
	
	@OnUIDestroy
	void destroyData() {
		totalSeconds = 0;
	    timer.cancel();
	    if (timer != null) {
	        timer.purge();
	        timer = null;
	        
	        findPasswordBtnField.setAttribute(AttributeKeys.enabled, true);
	        findPasswordBtnField.setValue("点击发送密码到手机");
	    }
	    
	    callBack.setPhoneNum("");
	    mobileNumField.setValue("");
	}
	
	@OnUICreate
	void onUiCreate() {
		
		totalSeconds = 60; 
		timer = new Timer();
	}
	
	@Command(description = "Invoke when a toolbar item was clicked", 
			uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	@Bean
	UserFindPswCallBack callBack = new UserFindPswCallBack();

	/**
	 * 发送密码到手机
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "sendPasswordToMobile", updateFields = {
					@FieldUpdating(fields={"mobileNum"},message="请输入正确的手机号")
			},
			navigations = { @Navigation(on = "StockAppBizException", message = "%m%n", params = {
			@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "错误") }) })
	@NetworkConstraint
	@ExeGuard(title = "重置密码", message = "正在处理，请稍候...", silentPeriod = 200)
	String sendPasswordToMobile(ExecutionStep step, InputEvent event, Object result) {
		
		String phoneNum = callBack.getPhoneNum();
		boolean isPhoneNum = Utils.getInstance().isMobileNum(phoneNum);
		
		if(!isPhoneNum) {
			throw new StockAppBizException("请输入正确的手机号");
		}
		
		switch (step) {
		case PROCESS:
			if (userService != null) {
				userService.resetPassword(callBack.getPhoneNum());
			}
			break;

		case NAVIGATION:
			if (timer != null) {
				task = new TimerTask() {

					@Override()
					public void run() {
						AppUtils.runOnUIThread(new Runnable() {

							@Override()
							public void run() {
								findPasswordBtnField
										.setValue("\u5bc6\u7801\u5df2\u53d1\u9001("
												+ totalSeconds + ")");
								totalSeconds--;
								if (totalSeconds < 0) {
									findPasswordBtnField
											.setValue("\u5982\u672a\u6536\u5230\uff0c\u70b9\u51fb\u91cd\u65b0\u53d1\u9001");
									totalSeconds = 60;
									findPasswordBtnField.setAttribute(
											AttributeKeys.enabled, true);
									cancel();
								}
							}
						});
					}
				};
				timer.schedule(task, 0, 1000);
				findPasswordBtnField.setAttribute(AttributeKeys.enabled, false);
			}
			break;
		default:
			break;
		}
		return null;
	}
}
