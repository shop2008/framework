package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.ExeGuard;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.FieldUpdating;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.OnUIDestroy;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.ValueType;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.IUICommandHandler.ExecutionStep;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.model.AuthInfo;
import com.wxxr.mobile.stock.app.model.UseSwitchCardCallBack;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "userSwitchCardPage", withToolbar = true, description = "更换银行卡")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.switch_bank_card_layout")
public abstract class UserSwitchCardPage extends PageBase {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Bean(type = BindingType.Pojo, express = "${usrService.userAuthInfo}")
	AuthInfo authBean;

	@Field(valueKey = "text", binding = "${authBean.accountName}")
	String accountName;

	@Field(valueKey = "text", binding = "${callBack.bankName}", attributes = { @Attribute(name = "text", value = "${authBean!=null?authBean.bankName:'--'}") })
	String bankName;

	@Field(valueKey = "text", binding = "${callBack.bankAddr}", attributes = { @Attribute(name = "text", value = "${authBean!=null?authBean.bankAddr:'--'}") })
	String bankAddr;

	@Field(valueKey = "text", binding = "${callBack.bankNum}", attributes = { @Attribute(name = "text", value = "${authBean!=null?authBean.bankNum:'--'}") })
	String bankNum;

	@Field(valueKey = "text", binding = "${callBack.accountName}",attributes = { @Attribute(name = "text", value = "${authBean!=null?authBean.accountName:'--'}") })
	String alteredAccountName;

	@Field(valueKey = "visible", binding = "${authBean.confirmed == true}")
	boolean confirmedBody;

	@Field(valueKey = "visible", binding = "${authBean.confirmed == false}")
	boolean unConfirmedBody;

	@Bean
	UseSwitchCardCallBack callBack = new UseSwitchCardCallBack();

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	/**
	 * 提交
	 * 
	 * @param event
	 * @return
	 */

	@Command(commandName = "commit", updateFields = { @FieldUpdating(fields = {
			"bankName", "bankAddr", "bankNum" }, message = "请输入正确的银行卡信息") }, navigations = { @Navigation(on = "StockAppBizException", message = "%m%n", params = {
			@Parameter(name = "autoClosed", type = ValueType.INETGER, value = "2"),
			@Parameter(name = "title", value = "错误") }) })
	@ExeGuard(title = "更换银行卡", message = "正在处理，请稍候...", silentPeriod = 200)
	String commit(ExecutionStep step, InputEvent event, Object result) {
		switch (step) {
		case PROCESS:
			if (usrService != null) {
				
				String accountName = null;
				
				if (authBean != null) {
					boolean confirmed = authBean.getConfirmed();
					if (confirmed) {
						accountName = authBean.getAccountName();
					} else {
						accountName = callBack.getAccountName();
					}
				}
				usrService.switchBankCard(accountName,
						callBack.getBankName(), callBack.getBankAddr(),
						callBack.getBankNum());

			}
			break;
		case NAVIGATION:
			hide();
			break;
		}
		return null;
	}

	@OnUIDestroy
	protected void clearData() {
		callBack.setAccountName("");
		callBack.setBankAddr("");
		callBack.setBankName("");
		callBack.setBankNum("");
	}
}
