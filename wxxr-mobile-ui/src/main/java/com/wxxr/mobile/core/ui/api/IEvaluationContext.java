/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IEvaluationContext {
	IUIComponent getField(String name);
	Object getBean(String name);
}
