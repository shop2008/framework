package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name="ResetPasswrodPage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.reset_passwrod_layout")
public abstract class ResetPasswrodPage extends PageBase {

	/**新密码*/
	@Field(valueKey="text")
	String newPassword;

	/**再次输入新密码*/
	@Field(valueKey="text")
	String reNewPassword;
	
	/**重置密码按钮*/
	@Field(valueKey="text")
	String resetPasswordBtn;
	
	/**重置密码*/
	@Command
	String resetPassword(InputEvent event) {
	
		//
		return null;
	}
}
