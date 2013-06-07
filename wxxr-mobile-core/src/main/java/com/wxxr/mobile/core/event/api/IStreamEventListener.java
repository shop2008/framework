/**
 * 
 */
package com.wxxr.mobile.core.event.api;

/**
 * @author neillin
 *
 */
public interface IStreamEventListener {
	void onEvent(IStreamEvent event,IListenerChain chain);
}
