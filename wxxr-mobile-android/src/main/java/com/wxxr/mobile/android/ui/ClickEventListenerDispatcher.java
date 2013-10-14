/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author neillin
 *
 */
public class ClickEventListenerDispatcher extends AbstractEventListenerDispatcher<OnClickListener> implements OnClickListener{
	public final static int TAG_ID = 1000;
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		OnClickListener[] listeners = getListeners(new OnClickListener[0]);
		for (OnClickListener l : listeners) {
			l.onClick(v);
		}
	}
}
