/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import java.util.HashMap;

import android.content.Intent;

import com.wxxr.mobile.android.ui.IAndroidPageNavigator;
import com.wxxr.mobile.android.ui.module.AbstractWorkbenchManagerModule;
import com.wxxr.mobile.core.ui.api.IEventBinderManager;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.IFieldBinderManager;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.common.WorkbenchBase;
import com.wxxr.mobile.stock.client.IStockAppContext;
import com.wxxr.mobile.stock.client.view.DeclarativePModelProvider;

/**
 * @author neillin
 *
 */
public class WorkbenchManagerModule extends AbstractWorkbenchManagerModule<IStockAppContext> {

	@Override
	protected void initFieldBinders(IFieldBinderManager mgr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initEventBinders(IEventBinderManager mgr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initAttributeUpdaters(IFieldAttributeManager mgr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initPresentationModels(IWorkbenchRTContext context) {
		DeclarativePModelProvider.updatePModel(context);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.module.AbstractWorkbenchManagerModule#doCreateWorkbench()
	 */
	@Override
	protected IWorkbench doCreateWorkbench() {
		return new WorkbenchBase(uiContext) {
			
			@Override
			public String[] getPageIds() {
				return manager.getAllRegisteredPageIds();
			}

			/* (non-Javadoc)
			 * @see com.wxxr.mobile.core.ui.common.WorkbenchBase#showHomePage()
			 */
			@Override
			public void showHomePage() {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(IAndroidPageNavigator.PARAM_KEY_INTENT_FLAG, String.valueOf(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				super.showPage(HOME_PAGE_ID,map,null);
			}
		};
	}

}
