/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.Map;

/**
 * @author neillin
 *
 */
public interface IBindingContext {
	IWorkbenchManager getWorkbenchManager();
	Map<String, String> getBindingAttrSet();
}
