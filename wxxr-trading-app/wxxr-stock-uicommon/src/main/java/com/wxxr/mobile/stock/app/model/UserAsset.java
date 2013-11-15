package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;
/**
 * 
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="UserAssetBean")
public class UserAsset {
	private long balance;//余额
	private long usableBal;//可用余额
	private long frozen;//冻结金额
}
