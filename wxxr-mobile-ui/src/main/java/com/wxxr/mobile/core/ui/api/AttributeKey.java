/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface AttributeKey<T> {
	Class<T> getValueType();
	String getName();

}
