package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
@View(name = "CreatePasswordPage" ,withToolbar=true, description="创建密码")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.create_password_layout")
public abstract class CreatePasswordPage extends PageBase {

	
	@Field(valueKey="text")
	String password;
	
	@Field(valueKey="text")
	String rePassword;
	
	@Command
	String done(InputEvent event) {
		return null;
	}
}
