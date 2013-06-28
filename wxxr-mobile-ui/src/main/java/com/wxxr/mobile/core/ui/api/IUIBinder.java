/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IUIBinder<T> {
	IBinding doBinding(T target);
}
