/**
 * 
 */
package com.wxxr.mobile.core.security.api;

/**
 * @author neillin
 *
 */
public interface IUserIdentityManager {
	boolean isUserAuthenticated();
	String getUserId();
	boolean usrHasRoles(String... roles);
}
