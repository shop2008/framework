/**
 * 
 */
package com.wxxr.mobile.stock.client.view;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.IAndroidBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.common.AbstractPageDescriptor;
import com.wxxr.mobile.stock.client.R;
import com.wxxr.mobile.stock.client.ui.HomePage;

/**
 * @author neillin
 *
 */
public class HomePageDescriptor extends AbstractPageDescriptor {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.AbstractPageDescriptor#createPageModel(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	@Override
	protected IPage createPageModel(IWorkbenchRTContext ctx) {
		HomePageModel page= new HomePageModel();
		page.setName(getViewId());
		return page;
	}

	@Override
	protected void init() {
		setViewId(IWorkbench.HOME_PAGE_ID);
		createViewGroup("contents").
			addView("introduce1").
			addView("introduce2").
			addView("introduce3");	
		addBindingDescriptor(TargetUISystem.ANDROID, new IAndroidBindingDescriptor() {
			
			@Override
			public String getBindingViewId() {
				return getViewId();
			}
			
			@Override
			public Class<?> getTargetClass() {
				return HomePage.class;
			}
			
			@Override
			public int getBindingLayoutId() {
				return R.layout.home_page;
			}

			@Override
			public AndroidBindingType getBindingType() {
				return AndroidBindingType.ACTIVITY;
			}
		});
	}

}
