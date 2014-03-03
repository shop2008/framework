/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface IValueEvaluator<T> {
	void doEvaluate();
	boolean valueEffectedBy(ValueChangedEvent event);
	List<String> getDependingBeans();
	void updateLocalValue(T val);
}
