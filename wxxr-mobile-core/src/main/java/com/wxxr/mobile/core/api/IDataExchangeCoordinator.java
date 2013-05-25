/**
 * 
 */
package com.wxxr.mobile.core.api;

/**
 * 
 * coordinate data exchange with server in network favor manner
 * @author neillin
 *
 */
public interface IDataExchangeCoordinator {
	
	void registerHandler(IExchangeHandler handler);
	
	boolean unregisterHandler(IExchangeHandler handler);

}
