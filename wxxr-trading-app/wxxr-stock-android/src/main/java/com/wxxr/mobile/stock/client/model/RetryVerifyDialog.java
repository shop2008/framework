package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="RetryVerifyDialog")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.error_verify_dialog")
public abstract class RetryVerifyDialog extends ViewBase {

	@Command(navigations={@Navigation(on="InputPswDialog", showDialog="InputPswDialog", closeCurrentView=true)})
	String reVerify(InputEvent event) {
		hide();
		return "InputPswDialog";
	}
	
	@Command
	String cancel(InputEvent event) {
		hide();
		return null;
	}
}
