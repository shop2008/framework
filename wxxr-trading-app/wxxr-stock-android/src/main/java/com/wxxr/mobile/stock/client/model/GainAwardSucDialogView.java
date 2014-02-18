package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="GainAwardSucDialogView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.gain_award_suc_dialog")
public abstract class GainAwardSucDialogView extends ViewBase {

	
	@Command
	String done(InputEvent event) {
		hide();
		AppUtils.getService(IWorkbenchManager.class).getPageNavigator().getCurrentActivePage().hide();
		return null;
	}

}
