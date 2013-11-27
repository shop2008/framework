/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IUIExceptionHandler {
	boolean handleException(IUIComponent component,Throwable t);
}
