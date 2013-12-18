package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="updateVertionDialog")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.update_vertion_dialog")
public abstract class UpdateVertionDialog extends ViewBase {

	
	@Command
	String downloadApk(InputEvent event) {
		//下载最新Apk
		
		hide();
		return null;
	}
	
	@Command
	String cancel(InputEvent event) {
		hide();
		return null;
	}
}
