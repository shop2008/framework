/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.widget.ListAdapter;

/**
 * @author neillin
 *
 */
public interface IRefreshableListAdapter extends ListAdapter {
	boolean refresh();
	void destroy();
}
