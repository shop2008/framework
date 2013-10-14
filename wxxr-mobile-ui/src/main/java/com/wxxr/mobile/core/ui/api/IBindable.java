/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IBindable<M extends IUIComponent> {
	void doBinding(IBinding<M> binding);
	boolean doUnbinding(IBinding<M> binding);
}
