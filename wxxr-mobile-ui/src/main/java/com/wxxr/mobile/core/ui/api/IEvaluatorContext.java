/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import com.wxxr.javax.el.ELContext;

/**
 * @author neillin
 *
 */
public interface IEvaluatorContext {
	IWorkbenchRTContext getUIContext();
	ELContext getELContext();
	Object getBean(String name);
}
