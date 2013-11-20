package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="curVertionDialog")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.current_vertion_dialog")
public abstract class CurVertionDialog extends ViewBase {

	@Command
	String known(InputEvent event) {
		hide();
		return null;
	}
}
