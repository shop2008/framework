package com.wxxr.mobile.stock.app.model;

import com.wxxr.javax.validation.constraints.NotNull;
import com.wxxr.javax.validation.constraints.Size;
import com.wxxr.mobile.stock.app.CrossFieldValidation;
import com.wxxr.mobile.stock.app.validate.NewPassword;

@NewPassword(groups=CrossFieldValidation.class)
public class UserAlterPswCallback {

	
	private String oldPassword;
	@NotNull
	@Size(min=6,max=12)
	private String newPassword;
	@NotNull
	@Size(min=6,max=12)
	private String newPasswordAgain;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordAgain() {
		return newPasswordAgain;
	}

	public void setNewPasswordAgain(String newPasswordAgain) {
		this.newPasswordAgain = newPasswordAgain;
	}
}
