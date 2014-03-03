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
	IView getView();
	Object getBean(String name);
}
