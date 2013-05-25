/**
 * 
 */
package com.wxxr.mobile.core.api;

/**
 * @author neillin
 *
 */
public interface PropertyEditor<T> {
	void setValue(T val);
	String getAsText();
	void setText(String txt);
	T getValue();
}
