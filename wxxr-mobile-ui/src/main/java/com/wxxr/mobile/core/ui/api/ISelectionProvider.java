/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface ISelectionProvider {
	ISelection getSelection();
	void addSelectionListener(ISelectionChangedListener listener);
	boolean removeSelectionListener(ISelectionChangedListener listener);
	String getProviderId();
}
