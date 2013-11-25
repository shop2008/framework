/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

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

}
