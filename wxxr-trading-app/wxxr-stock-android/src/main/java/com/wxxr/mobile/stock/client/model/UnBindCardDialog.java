package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name = "unBindCardDialog")
@AndroidBinding(type = AndroidBindingType.ACTIVITY, layoutId = "R.layout.unbind_card_dialog_layout")
public abstract class UnBindCardDialog extends PageBase {

	/**
	 * 绑定-跳转到"我的认证"界面
	 * 
	 * @param event
	 * @return
	 */
	@Command(commandName = "done", navigations = { @Navigation(on = "SUCCESS", showPage = "userAuthPage") })
	String done(InputEvent event) {
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
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
		getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
		return null;
	}
}
