package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.stock.app.LoginFailedException;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;

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
	 * 
	 * 注册用户-用户名密码方式注册
	 * @param userName 用户名
	 * @param pass 密码
	 * @param pass2 重新输入密码
	 * @throws StockAppBizException - 如果不符合规范或者用户已注册，将抛出异常
	 */
	void register(String userName,String pass,String pass2) throws StockAppBizException;
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
	
	/**
	 * 获取他人主页
	 * @param userId
	 * @return
	 */
	PersonalHomePageBean getOtherPersonalHomePage(String userId);
	/**
	 * 获取他人主页更多条数地址
	 * @param userId 用户ID
	 * @param start
	 * @param limit
	 * @param virtual - true：虚拟盘，false；实盘
	 * @return
	 * @throws Exception
	 */
	BindableListWrapper<GainBean> getMoreOtherPersonal(String userId, int start, int limit, boolean virtual);
}
