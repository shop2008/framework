/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IObservableListDataProvider extends IListDataProvider {
	void registerDataChangedListener(IDataChangedListener listener);
	boolean unregisterDataChangedListener(IDataChangedListener listener);
}
