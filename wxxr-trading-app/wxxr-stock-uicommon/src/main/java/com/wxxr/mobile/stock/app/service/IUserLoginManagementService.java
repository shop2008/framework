package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.stock.app.LoginFailedException;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.UserBean;

public interface IUserLoginManagementService {

	/**
	 * 注册用户-使用手机号注册
	 * 用户使用手机号注册，如果没有注册过，则直接使用短信把密码发给他
	 * 
	 * @param phoneNumber - 用户手机号
	 * @throws StockAppBizException 如果已经注册过，抛出异常-通知客户端已经注册
	 */
	void register(String phoneNumber) throws StockAppBizException;

	 
	/**
	 * 用户登陆
	 * @param userId
	 * @param pwd
	 * @throws StockAppBizException
	 */
	void login(String userId,String pwd) throws LoginFailedException;
	/**
	 * 退出登陆
	 * @param userId
	 * @param pwd
	 */
	void logout();

	void resetPassword(String userName);
	
	UserBean getMyUserInfo();
}
