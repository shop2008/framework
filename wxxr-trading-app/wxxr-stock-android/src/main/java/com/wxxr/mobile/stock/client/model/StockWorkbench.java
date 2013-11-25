/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;

import android.content.Intent;

import com.wxxr.mobile.android.ui.IAndroidPageNavigator;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Workbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.common.WorkbenchBase;

/**
 * @author neillin
 *
 */
@View
@Workbench(title="Mobile Stock client",description="Mobile Stock client description",defaultNavigations={
		@Navigation(on="LoginFailedException",message="show login failed message"),
		@Navigation(on="Thorwable", message="Caught unexpecting exception , please call ...")
})
public class StockWorkbench extends WorkbenchBase {

	public StockWorkbench(IWorkbenchRTContext ctx) {
		super(ctx);
	}

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.WorkbenchBase#showHomePage()
	 */
	@Override
	public void showHomePage() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(IAndroidPageNavigator.PARAM_KEY_INTENT_FLAG, String.valueOf(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		super.showPage(HOME_PAGE_ID,map,null);
	}

}
