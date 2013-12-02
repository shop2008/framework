package com.wxxr.mobile.stock.client.model;

import java.util.Map;

import android.text.TextUtils;

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
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.stock.app.model.UseSwitchCardCallBack;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name = "userSwitchCardPage")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.switch_bank_card_layout")
public abstract class UserSwitchCardPage extends PageBase implements
		IModelUpdater {

	@Bean(type = BindingType.Service)
	IUserManagementService usrService;

	@Field(valueKey = "text", binding = "${accountName}")
	String accountName;

	@Field(valueKey = "text", binding = "${callBack.bankName}")
	String bankName;

	@Field(valueKey = "text", binding = "${callBack.bankAddr}")
	String bankAddr;

	@Field(valueKey = "text", binding = "${callBack.bankNum}")
	String bankNum;

	@Bean
	UseSwitchCardCallBack callBack = new UseSwitchCardCallBack();

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
	@ExeGuard(title = "更换银行卡", message = "正在处理，请稍候...", silentPeriod = 1)
	String commit(InputEvent event) {
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			if (usrService != null)
				usrService.switchBankCard(callBack.getBankName(),
						callBack.getBankAddr(), callBack.getBankNum());
		}
		return null;
	}

	@Override
	public void updateModel(Object value) {

		Map<String, String> map = (Map<String, String>) value;

		String accountName = map.get("accountName");

		if (!TextUtils.isEmpty(accountName)) {
			registerBean("accountName", accountName);
		}
	}
}
