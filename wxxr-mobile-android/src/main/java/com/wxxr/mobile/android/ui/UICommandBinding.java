/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.view.View;
import android.view.View.OnClickListener;

import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUIComponent;

/**
 * @author neillin
 *
 */
public class UICommandBinding extends BasicFieldBinding {

	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			((IUICommand)getField()).execute();
		}
	};
	
	public UICommandBinding(View view, IUIComponent field) {
		super(view, field);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.BasicFieldBinding#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		getComponent().setClickable(true);
		getComponent().setOnClickListener(listener);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.BasicFieldBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		getComponent().setOnClickListener(null);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.BasicFieldBinding#updateUI(boolean)
	 */
	@Override
	public void updateUI(boolean recursive) {
		super.updateUI(recursive);
	}

}
