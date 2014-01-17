package com.wxxr.mobile.stock.client.model;


import android.os.SystemClock;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
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
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.model.AuthInfo;
import com.wxxr.mobile.stock.app.model.UseSwitchCardCallBack;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "userSwitchCardPage",withToolbar=true, description="更换银行卡")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.switch_bank_card_layout")
public abstract class UserSwitchCardPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	
	@Bean(type=BindingType.Pojo,express="${usrService.userAuthInfo}")
	AuthInfo authBean;
	
	@Field(valueKey = "text", binding = "${authBean.accountName}")
	String accountName;

	@Field(valueKey = "text", binding = "${callBack.bankName}")
	String bankName;

	@Field(valueKey = "text", binding = "${callBack.bankAddr}")
	String bankAddr;

	@Field(valueKey = "text", binding = "${callBack.bankNum}")
	String bankNum;

	@Bean
	UseSwitchCardCallBack callBack = new UseSwitchCardCallBack();

	@Menu(items = { "left" })
	private IMenu toolbar;
	
	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}
	
	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "back", description = "Back To Last UI")
	String back(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			getUIContext().getWorkbenchManager().getPageNavigator()
					.hidePage(this);
		}
		return null;
	}

	/**
	 * 提交
	 * 
	 * @param event
	 * @return
	 */

	@Command(commandName = "commit", navigations = { @Navigation(on = "StockAppBizException", message = "%m%n", params = {
			@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "错误")}) })
	@ExeGuard(title = "更换银行卡", message = "正在处理，请稍候...", silentPeriod = 200)
	String commit(InputEvent event) {
		SystemClock.sleep(500);
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			if (usrService != null)
				usrService.switchBankCard(callBack.getBankName(),
						callBack.getBankAddr(), callBack.getBankNum());
		}
		hide();
		return null;
	}

	@OnUIDestroy
	protected void clearData() {
		callBack.setBankAddr("");
		callBack.setBankName("");
		callBack.setBankNum("");
	}
}
