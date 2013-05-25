/**
 * 
 */
package com.wxxr.mobile.core.api;

/**
 * @author neillin
 *
 */
public interface IUserAuthManager {
	IUserAuthCredential getAuthCredential(String host, String realm);
}
