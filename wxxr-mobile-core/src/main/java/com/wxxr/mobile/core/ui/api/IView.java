/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IView {
	<T> T getModel(Class<T> clazz);
}
