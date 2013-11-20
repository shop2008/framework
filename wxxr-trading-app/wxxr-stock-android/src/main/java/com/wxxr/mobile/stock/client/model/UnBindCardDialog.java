package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name = "unBindCardDialog")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.unbind_card_dialog_layout")
public abstract class UnBindCardDialog extends ViewBase {

	/**
	 * 绑定-跳转到"提现认证"界面
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "done", navigations = { @Navigation(on = "SUCCESS", showPage = "withDrawCashAuthPage") })
	String done(InputEvent event) {
		return "SUCCESS";
	}

	/**
	 * 取消绑定
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "cancel")
	String cancel(InputEvent event) {

		/** 取消 */
		hide();
		return null;
	}
}
