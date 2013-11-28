/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.stock.app.LoginFailedException;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuthInfoBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.ScoreInfoBean;
import com.wxxr.mobile.stock.app.bean.TradeDetailListBean;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.bean.VoucherBean;

/**
 * @author neillin
 *
 */
public interface IUserManagementService {
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	UserBean getMyUserInfo();
	/**
	 * 获取他人用户信息
	 * @return
	 */
	UserBean getUserInfoById(String userId);
	/**
	 * 注册用户-使用手机号注册
	 * 用户使用手机号注册，如果没有注册过，则直接使用短信把密码发给他
	 * 
	 * @param phoneNumber - 用户手机号
	 * @throws StockAppBizException 如果已经注册过，抛出异常-通知客户端已经注册
	 */
	void register(String phoneNumber) throws StockAppBizException;
	/**
	 * 注册用户-普通注册方式
	 * 
	 * @param userId
	 * @param password
	 * @throws StockAppBizException 如果已经注册过，抛出异常-通知客户端已经注册
	 */
	 void register(String userId,String password) throws StockAppBizException;
	 
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
	/**
	 * 修改密码
	 * @param oldPwd - 旧密码
	 * @param newPwd - 新密码
	 * @throws StockAppBizException
	 */
	void updatePassword(String oldPwd,String newPwd) throws StockAppBizException;
	/**
	 * 检查用户是否登陆，如果用户未登录则会弹出登陆框
	 * @return
	 */
	void checkLogin();
	/**
	 * 手机认证-绑定手机号
	 * @param phoneNumber - 手机号
	 * @param vertifyCode - 认证码
	 */
	void bindMobile(String phoneNumber,String vertifyCode) throws StockAppBizException;
	/**
	 * 更改手机绑定
	 * @param phoneNumber - 手机号
	 * @param vertifyCode - 认证码
	 */
	void changeBindingMobile(String phoneNumber,String vertifyCode);
	/**
	 * 设置是否推送消息
	 * @param on 
	 * 		true 推送消息开启
	 * 		false 推送消息关闭
	 */
	void pushMessageSetting(boolean on);

	/**
	 * 获取推送消息是否开启
	 * @return 
	 *       true  推送消息 
	 *       false  不推送消息
 	 */
	boolean getPushMessageSetting();
	
	/**
	 * 设置是否阅读了注册规则，如果未阅读不准注册，阅读后才可以注册
	 * @param isRead
	 * 		true 已阅读了注册规则
	 * 		false 未阅读注册规则
	 */
	void setRegRulesReaded(boolean isRead);
	
	
	/**
	 * 判断当前用户是否绑定了银行卡
	 * @return 
	 *  	true 已绑定
	 *  	false 未绑定
	 */
	boolean isBindCard();
	
	
	/**
	 * 绑定银行卡
	 * @param accountName 户名
	 * @param bankName 开户行
	 * @param bankAddr 开户行地址
	 * @param bankNum 银行账号
	 * @return 
	 * 		true 绑定成功
	 *      false 绑定失败
	 */
	boolean bindBankCard(String accountName,String bankName,String bankAddr, String bankNum);

	
	ScoreInfoBean fetchUserScoreInfo(String userId);
	
	boolean switchBankCard(String accountName, String bankName, String bankAddr,String bankNum);
	
	/**
	 * 提现认证
	 * @param accountName 用户名
	 * @param bankName 开户行名称
	 * @param bankAddr 开户行所在地
	 * @param bankNum 银行账号
	 * @return true 认证成功， false 认证失败
	 */
	boolean withDrawCashAuth(String accountName, String bankName, String bankAddr,String bankNum);

	
	/**
	 * 获取用户认证信息--提现认证
	 * @return null 未认证， 非空为已认证
	 */
	AuthInfoBean getUserAuthInfo();
	
	
	/**
	 * 获取认证手机号
	 * @return 认证的手机号码
	 */
	String getUserAuthMobileNum(String userId);
	
	
	/**
	 * 获取当前用户的实盘积分明细
	 * @return 
	 * 		如果有记录返回记录信息，否则返回空
	 */
	ScoreInfoBean getMyUserScoreInfo();
	
	/**
	 * 获取当前用户余额明细
	 * @return 
	 * 		如果有记录返回记录信息，否则返回空
	 */	
	TradeDetailListBean getMyTradeDetailInfo();
	/**
	 * 获取他人主页
	 * @param userId
	 * @return
	 */
	PersonalHomePageBean getOtherPersonalHomePage(String userId);
	/**
	 * 获取当前用户个人主页
	 * @param userId
	 * @return
	 */
	PersonalHomePageBean getMyPersonalHomePage();
	/**
	 * 获取当前用户自己的主页更多条数地址
	 * @param start
	 * @param limit
	 * @param virtual - true：虚拟盘，false；实盘
	 * @return
	 * @throws Exception
	 */
	PersonalHomePageBean getMorePersonalRecords(int start,int limit,boolean virtual);
	/**
	 * 获取他人主页更多条数地址
	 * @param userId 用户ID
	 * @param start
	 * @param limit
	 * @param virtual - true：虚拟盘，false；实盘
	 * @return
	 * @throws Exception
	 */
	PersonalHomePageBean getMoreOtherPersonal(String userId, int start, int limit, boolean virtual);
	
	/**
	 * 获取自己的账户
	 * @return
	 */
	UserAssetBean getUserAssetBean();
	VoucherBean getVoucherBean();
}
