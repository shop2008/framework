/**
 * 
 */
package com.wxxr.mobile.core.session.api;

/**
 * @author neillin
 *
 */
public interface ISessionManager {
	ISession getCurrentSession(boolean createIfNotExisting);
	void destroySession(ISession session);
}
