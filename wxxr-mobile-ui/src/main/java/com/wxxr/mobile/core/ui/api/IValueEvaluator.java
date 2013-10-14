/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IValueEvaluator {
	void doEvaluate(IEvaluationContext ctx);
	boolean valueEffectedBy(ValueChangedEvent event);
}
