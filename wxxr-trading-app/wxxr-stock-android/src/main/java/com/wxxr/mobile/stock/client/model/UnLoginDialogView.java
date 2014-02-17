package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="UnLoginDialogView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.unlogin_dialog")
public abstract class UnLoginDialogView extends ViewBase {

	@Command(navigations={@Navigation(on="*", showPage="userLoginPage")})
	String done(InputEvent event) {
		hide();
		return "";
	}
	
	@Command
	String cancel(InputEvent event) {
		hide();
		return null;
	}
}
