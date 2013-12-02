/**
 * 
 */
package com.wxxr.mobile.core.ui.api;



/**
 * @author dz
 *
 */
public interface IViewPagerDataProvider {
	int getItemCounts();
	Object getItem(int i);
//	Object getItemId(Object item);
//	boolean isItemEnabled(Object item);
	boolean updateDataIfNeccessary();
}
