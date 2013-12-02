package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.model.UserDrawCachAuthCallBack;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "withDrawCashAuthPage")
@AndroidBinding(type = AndroidBindingType.ACTIVITY, layoutId = "R.layout.withdraw_cash_auth_layout")
public abstract class UserWithDrawCashAuthPage extends PageBase {

	/**
	 * 户名
	 */
	@Field(valueKey = "text", binding = "${callBack.accountName}")
	String accountName;

	/**
	 * 银行名称
	 */
	@Field(valueKey = "text", binding = "${callBack.bankName}")
	String bankName;

	/**
	 * 开户行所在地
	 */
	@Field(valueKey = "text", binding = "${callBack.bankAddr}")
	String bankAddr;

	/**
	 * 银行卡号
	 */
	@Field(valueKey = "text", binding = "${callBack.bankNum}")
	String bankNum;

	@Bean(type = BindingType.Service)
	IUserManagementService userService;

	@Bean
	UserDrawCachAuthCallBack callBack = new UserDrawCachAuthCallBack();

	/**
	 * 标题栏-"返回"按钮事件处理
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "back")
	String back(InputEvent event) {

		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}

	/**
	 * 提现认证处理
	 * @param event
	 * @return
	 */
	@Command(commandName = "cashAuth", description = "Back To Last UI", navigations = { @Navigation(on = "StockAppBizException", message = "%m%n", params = {
			@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "错误") }) })
	@ExeGuard(title = "提现认证", message = "正在处理，请稍候...", silentPeriod = 1)
	String cashAuth(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			if (userService != null) {
				userService.withDrawCashAuth(callBack.getAccountName(),
						callBack.getBankName(), callBack.getBankAddr(),
						callBack.getBankNum());
			}
			hide();
		}
		return null;
	}

}
