/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.Map;

/**
 * @author neillin
 *
 */
public interface INavigationDescriptor {
	String getResult();
	String getMessage();
	String getToView();
	String getToPage();
	Map<String, Object> getParameters();
}
