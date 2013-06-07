/**
 * 
 */
package com.wxxr.mobile.core.event.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface IListenerChain {
	void invokeNext(IStreamEvent event);
	
	void setAttribute(String name, Object val);
	Object getAttribute(String name);
	List<String> getAttributeNames();
}
