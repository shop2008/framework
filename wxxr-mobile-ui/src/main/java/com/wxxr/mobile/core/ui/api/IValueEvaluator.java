/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IValueEvaluator<T> {
	T doEvaluate();
	boolean valueEffectedBy(ValueChangedEvent event);
}
