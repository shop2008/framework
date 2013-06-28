/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IBindable {
	void addBinding(IBinding binding);
	boolean removeBinding(IBinding binding);
}
