/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.view.View;

import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewFrame;

/**
 * @author neillin
 *
 */
public class ViewFrameBinding extends UIContainerBinding {

	public ViewFrameBinding(View view, IUIComponent field) {
		super(view, field);
	}


	/**
	 * @param view
	 * @return
	 */
	protected String getUIName(IUIComponent field) {
		if(field instanceof IView){
			return "v_"+field.getName();
		}
		throw new IllegalArgumentException("Unknown field :"+field);
	}


	@Override
	protected String getChildBindingFactoryName(IUIComponent view, View ui) {
		return "view_binding";
	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.BasicFieldBinding#setUIVisibility()
	 */
	@Override
	protected void setUIVisibility() {
		IViewFrame field = (IViewFrame)getField();
		View view = getComponent();
		if(view.getVisibility() == View.VISIBLE){
			field.setVisible(true);
		}else{
			field.setVisible(false);
		}
	}

}
