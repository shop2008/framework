/**
 * 
 */
package com.wxxr.mobile.stock.client.service;

import java.util.List;

import com.wxxr.mobile.stock.client.bean.Score;
import com.wxxr.mobile.stock.client.bean.ScoreInfoEntity;
import com.wxxr.mobile.stock.client.bean.User;
import com.wxxr.mobile.stock.client.bean.UserInfoEntity;

/**
 * @author neillin
 *
 */
public interface IUserManagementService {
	UserInfoEntity getMyInfo();
	User fetchUserInfo();
	void register(String userId);
	void login(String userId,String pwd);
	
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

	
	ScoreInfoEntity fetchUserScoreInfo(String userId);
}
