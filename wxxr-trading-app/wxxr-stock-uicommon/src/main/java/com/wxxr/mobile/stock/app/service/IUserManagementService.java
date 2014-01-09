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
import com.wxxr.mobile.stock.app.bean.ScoreInfoBean;
import com.wxxr.mobile.stock.app.bean.TradeDetailListBean;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.bean.UserBean;
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
	
	


	
	ScoreInfoBean fetchUserScoreInfo(String userId);
	
	
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
	BindableListWrapper<GainBean> getMorePersonalRecords(int start,int limit,boolean virtual);
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
}
