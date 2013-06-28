/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.view.View;

import com.wxxr.mobile.core.ui.api.DataChangedEvent;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IViewFrame;

/**
 * @author neillin
 *
 */
public class UIBinding extends UIContainerBinding implements IBinding {
		
	public UIBinding(IPage p, BindableActivity activity){
		super(activity.getLayoutView(),p);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBinding#notifyDataChanged(com.wxxr.mobile.core.ui.api.DataChangedEvent)
	 */
	@Override
	public void notifyDataChanged(DataChangedEvent event) {
		handleDataChanged(event);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IBinding#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		updateUI(true);
	}


	@Override
	protected String getUIName(IUIComponent field) {
		if(field instanceof IViewFrame){
			return "frm_"+field.getName();
		}
		throw new IllegalArgumentException("UNKnown field :"+field);
	}

	@Override
	protected String getChildBindingFactoryName(IUIComponent view, View ui) {
		return "frame_binding";
	}
	

	public IPage getPage() {
		return (IPage)getField();
	}
	
}
