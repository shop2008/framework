package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.service.IUserManagementService;
import com.wxxr.mobile.stock.client.utils.ColorUtils;
@View(name = "userRegPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.quick_register_layout")
public abstract class UserRegPage extends PageBase {

	static Trace log = Trace.register(UserRegPage.class);
	@Field(valueKey = "text")
	String mobileNum;
	DataField<String> mobileNumField;
	
	@Field(valueKey="text")
	String registerBtn;
	
	@Field
	IUserManagementService userService;

	/**
	 * 是否阅读了《注册条款》
	 */
	@Field(valueKey="checked")
	boolean readChecked;
	
	DataField<Boolean> readCheckedField;
	
	DataField<String> registerBtnField;
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
	 * 手机号码编辑框
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName="mnTextChanged")
	String mnTextChanged(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_TEXT_CHANGED)) {
			String mobileNum = (String) event.getProperty("changedText");
			mobileNumField.setValue(mobileNum);
		}
		return null;
	}

	/**
	 * 将密码发送到手机
	 * @param event
	 * @return
	 */
	@Command(commandName = "sendMsg")
	String sendMsg(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			//将密码发送到手机
			if (log.isDebugEnabled()) {
				log.debug("register:Send Message To Mobile");
			}
				try {
					getUIContext().getKernelContext().getService(IUserManagementService.class).register(mobileNumField.getValue());
				} catch (StockAppBizException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return null;
	}
	
	
	/**
	 * 转向《注册规则》详细界面注册规则
	 * @param event
	 * @return
	 */
	@Command(commandName = "registerRules")
	String registerRules(InputEvent event) {

		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getWorkbench().showPage("registerRulesPage", null, null);
		}
		return null;
	}
	
	/**
	 * 设置CheckBox是否选中
	 * @param event InputEvent.EVENT_TYPE_CLICK
	 * @return null
	 */
	@Command(commandName="setReadChecked")
	String setReadChecked(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			
			System.out.println("-------------123");
			if (this.readChecked == true) {
				this.readChecked = false;
				this.readCheckedField.setValue(false);
				
				this.registerBtnField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_GRAY);
				this.registerBtnField.setAttribute(AttributeKeys.enabled, false);
			} else {
				this.readChecked = true;
				this.readCheckedField.setValue(true);
				
				this.registerBtnField.setAttribute(AttributeKeys.textColor, ColorUtils.STOCK_WHITE);
				this.registerBtnField.setAttribute(AttributeKeys.enabled, true);
			}
			//userService.setRegRulesReaded(this.readCheckedField.getValue());
		
			
		}
		return null;
	}
	@OnShow
	protected void initData() {
		
		userService = getUIContext().getKernelContext().getService(IUserManagementService.class);
	}
}
