/**
 * 
 */
package com.wxxr.mobile.stock.client.service;

import com.wxxr.mobile.stock.client.bean.User;
import com.wxxr.mobile.stock.client.bean.UserInfoEntity;

/**
 * @author neillin
 *
 */
public interface IUserManagementService {
	UserInfoEntity getMyInfo();
	User fetchUserInfo();
}
