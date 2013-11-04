/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface ISelectionService {
	void addSelectionListener(ISelectionChangedListener listener);
	void addSelectionListener(String providerId,ISelectionChangedListener listener);
	boolean removeSelectionListener(ISelectionChangedListener listener);
	boolean removeSelectionListener(String providerId,ISelectionChangedListener listener);
	ISelection getSelection();
	ISelection getSelection(String providerId);
	void registerProvider(ISelectionProvider provider);
	boolean unregisterProvider(ISelectionProvider provider);
}
