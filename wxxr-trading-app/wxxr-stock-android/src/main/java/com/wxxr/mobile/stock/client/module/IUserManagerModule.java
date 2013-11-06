/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import com.wxxr.mobile.stock.client.StockAppBizException;

/**
 * @author wangxuyang
 *
 */
public interface IUserManagerModule {
	void login(String userId,String password) throws StockAppBizException;
	void register(String userId,String password) throws StockAppBizException;
	void logout(String userId);
	void resetPassword(String userId,String oldPassword,String newPassword) throws StockAppBizException;
}
