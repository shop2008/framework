/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.view.View;

import com.wxxr.mobile.core.ui.api.IUIComponent;

/**
 * @author neillin
 *
 */
public class ViewBindingFactory implements IFieldBindingFactory {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IFieldBindingFactory#getName()
	 */
	@Override
	public String getName() {
		return "frame_binding";
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.IFieldBindingFactory#createBinding(com.wxxr.mobile.android.ui.IBindingContext, android.view.View, com.wxxr.mobile.core.ui.api.IUIComponent)
	 */
	@Override
	public IFieldBinding createBinding(IBindingContext context, View pField,
			IUIComponent lField) {
		return new ViewFrameBinding(pField, lField);
	}

}
