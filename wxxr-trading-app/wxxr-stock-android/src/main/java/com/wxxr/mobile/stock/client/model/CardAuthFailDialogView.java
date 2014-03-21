package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="CardAuthFailDialogView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.card_auth_fail_dialog")
public abstract class CardAuthFailDialogView extends ViewBase {

	@Command
	String done(InputEvent event) {
		
		hide();
		AppUtils.getService(IWorkbenchManager.class).getPageNavigator().getCurrentActivePage().hide();
		return null;
	}
}
