/**
 * 
 */
package com.wxxr.mobile.core.session.api;

import com.wxxr.mobile.core.event.api.IBroadcastEvent;

/**
 * @author neillin
 *
 */
public interface ISessionEvent extends IBroadcastEvent {
	ISession getSession();
	SessionAction getAction();
}
