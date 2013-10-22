/**
 * 
 */
package com.wxxr.mobile.stock.client.view;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.IAndroidBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.common.AbstractViewDescriptor;
import com.wxxr.mobile.stock.client.R;

/**
 * @author neillin
 *
 */
public class LeftMenuViewDescriptor extends AbstractViewDescriptor {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.AbstractViewDescriptor#createPModel(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	@Override
	protected IView createPModel(IWorkbenchRTContext arg0) {
		return new LeftMenuViewModel();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.AbstractViewDescriptor#init()
	 */
	@Override
	protected void init() {
		setViewId("leftMenu");
		addBindingDescriptor(TargetUISystem.ANDROID, new IAndroidBindingDescriptor() {
			
			@Override
			public String getBindingViewId() {
				return getViewId();
			}
			
			@Override
			public Class<?> getTargetClass() {
				return null;
			}
			
			@Override
			public int getBindingLayoutId() {
				return R.layout.layout_left_navi_item;
			}

			@Override
			public AndroidBindingType getBindingType() {
				return AndroidBindingType.VIEW;
			}
		});

	}

}
