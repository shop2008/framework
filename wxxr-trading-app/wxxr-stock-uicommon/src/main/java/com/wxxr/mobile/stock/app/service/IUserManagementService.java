/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.ClientInfoBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.bean.PersonalHomePageBean;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
import com.wxxr.mobile.stock.app.bean.UserSignBean;
import com.wxxr.mobile.stock.app.bean.VoucherBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.model.AuthInfo;

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

	boolean verfiy(String userId,String password);
	 

	/**
	 * 修改密码
	 * @param oldPwd - 旧密码
	 * @param newPwd - 新密码
	 * @param newPwd2 重复新密码
	 * @throws StockAppBizException
	 */
	void updatePassword(String oldPwd,String newPwd,String newPwd2) throws StockAppBizException;

	/**
	 * 设置是否推送消息
	 * @param on 
	 * 		true 推送消息开启
	 * 		false 推送消息关闭
	 */
	void pushMessageSetting(boolean on);

	
	void switchBankCard(String bankName, String bankAddr,String bankNum);
	/**
	 * 提现认证
	 * @param accountName 用户名
	 * @param bankName 开户行名称
	 * @param bankAddr 开户行所在地
	 * @param bankNum 银行账号
	 * @return true 认证成功， false 认证失败
	 */
	void withDrawCashAuth(String accountName, String bankName, String bankAddr,String bankNum);

	
	/**
	 * 获取用户认证信息--提现认证
	 * 返回的 AuthInfoBean 如果为空则就是未认证
	 * @return  
	 */
	AuthInfo getUserAuthInfo();
	
	
	/**
	 * 获取当前用户个人主页
	 * @param userId
	 * @return
	 */
	PersonalHomePageBean getMyPersonalHomePage(boolean isAsync);

	/**
	 * 获取当前用户自己的主页更多条数地址
	 * @param start
	 * @param limit
	 * @param virtual - true：虚拟盘，false；实盘
	 * @return
	 * @throws Exception
	 */
	BindableListWrapper<GainBean> getMorePersonalRecords(int start,int limit,boolean virtual);
	BindableListWrapper<GainBean> getMorePersonalRecords(int start,int limit,boolean virtual,boolean wait4Finish);

	
	/**
	 * 获取自己的账户
	 * @return
	 */
	UserAssetBean getUserAssetBean();
	VoucherBean getVoucherBean();
	/**
	 * 获取提醒
	 * @return
	 */
	BindableListWrapper<RemindMessageBean> getRemindMessageBean();
	BindableListWrapper<PullMessageBean> getPullMessageBean(int start,int limit);
	BindableListWrapper<PullMessageBean> getPullMessageBean(int start,int limit,boolean wait4Finish);
	void updateNickName(String nickName);
	/**
	 * 收支明细
	 * @param start
	 * @param limit
	 * @return
	 */
	BindableListWrapper<GainPayDetailBean> getGPDetails(int start,int limit);
	
	UserBean refreshUserInfo();
	

	
	void readRemindMessage(String id);
	BindableListWrapper<RemindMessageBean> getUnreadRemindMessages();
	void readAllUnremindMessage();
	void readPullMesage(long id);
	
	ClientInfoBean getClientInfo();
	
	UserSignBean getUserSignBean();
	UserSignBean sign();
	
	String getGuideGainRule();
	void getGuideGain();
}
