package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
@View(name="noVerUpdateDialog")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.no_update_dialog")
public abstract class NoVerUpdateDialog extends ViewBase {

	@Command
	String done(InputEvent event) {
		hide();
		return null;
	}

}
