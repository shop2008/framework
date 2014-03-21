/**
 * 
 */
package com.wxxr.mobile.stock.client.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

/**
 * @author neillin
 *
 */
public class PullToRefreshView extends RefreshableLayout {

	/**
	 * @param context
	 * @param attrs
	 */
	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 */
	public PullToRefreshView(Context context) {
		super(context);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.widget.PullToRefreshView#getPullableView(android.view.View)
	 */
	@Override
	protected IPullableView getPullableView(View child) {
		if(child instanceof AdapterView){
			return new PullableAdapterView((AdapterView<?>)child);
		}
		if(child instanceof ScrollView){
			return new PullableScrollView((ScrollView)child);
		}
		return null;
	}

}
