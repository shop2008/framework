/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

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
	<T extends ISelection> T getSelection(Class<T> clazz);
	<T extends ISelection> List<T> getSelections(Class<T> clazz);
	void registerProvider(ISelectionProvider provider);
	boolean unregisterProvider(ISelectionProvider provider);
	String getCurrentProviderId();
}
