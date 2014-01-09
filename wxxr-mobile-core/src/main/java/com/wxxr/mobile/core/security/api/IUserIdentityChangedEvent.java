/**
 * 
 */
package com.wxxr.mobile.core.security.api;

import com.wxxr.mobile.core.event.api.IBroadcastEvent;

/**
 * @author neillin
 *
 */
public interface IUserIdentityChangedEvent extends IBroadcastEvent {
	IUserIdentityManager getIdentityManager();
}
