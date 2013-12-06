/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.core.ui.api.IUIComponent;

/**
 * @author neillin
 *
 */
public interface IListAdapterBuilder {
	IRefreshableListAdapter buildListAdapter(IUIComponent field, IAndroidBindingContext bContext,String itemViewId);
}
