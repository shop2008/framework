/**
 * 
 */
package com.wxxr.mobile.core.security.api;

import com.wxxr.mobile.core.event.api.IBroadcastEvent;

/**
 * @author neillin
 *
 */
public interface IUserLoginEvent extends IBroadcastEvent {
	LoginAction getAction();
	String getUserId();
}
