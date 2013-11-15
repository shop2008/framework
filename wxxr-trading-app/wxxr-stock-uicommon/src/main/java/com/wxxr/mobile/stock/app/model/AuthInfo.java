package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;

import android.R.string;
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="AuthInfoBean")
public class AuthInfo {

	/**用户名*/
	private String accountName;
	
	/**开户行*/
	private String bankName;
	
	/**开户行所在地*/
	private String bankAddr;
	
	/**银行卡号*/
	private String bankNum;
}
