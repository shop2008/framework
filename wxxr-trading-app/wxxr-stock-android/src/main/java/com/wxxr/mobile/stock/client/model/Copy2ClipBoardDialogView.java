package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="Copy2ClipBoardDialogView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.copy_to_clipboard_layout")
public abstract class Copy2ClipBoardDialogView extends ViewBase {

	@Command
	String done(InputEvent event) {
		hide();
		//AppUtils.getService(IWorkbenchManager.class).getPageNavigator().getCurrentActivePage().hide();
		return null;
	}
}
