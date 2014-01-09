/**
 * 
 */
package com.wxxr.mobile.core.security.api;

/**
 * @author neillin
 *
 */
public interface IUserIdentityManager {
	String UNAUTHENTICATED_USER_ID = "anonymouse";
	
	boolean isUserAuthenticated();
	String getUserId();
	boolean usrHasRoles(String... roles);
}
