/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public interface IListDataProvider {
	int getItemCounts();
	Object getItem(int i);
	Object getItemId(Object item);
}
