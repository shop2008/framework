package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="WaitVerifyDialogView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.wait_verify_dialog")
public abstract class WaitVerifyDialogView extends ViewBase {

	@Command
	String cancel(InputEvent event) {
		hide();
		return null;
	}

}
