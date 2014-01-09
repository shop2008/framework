/**
 * 
 */
package com.wxxr.mobile.core.session.api;

/**
 * @author neillin
 *
 */
public interface ISession {
	Object getValue(String name);
	Object removeValue(String name);
	void setValue(String name, Object val);
	String[] getValueNames();
	boolean hasValue(String name);
	String getSessionId();
}
