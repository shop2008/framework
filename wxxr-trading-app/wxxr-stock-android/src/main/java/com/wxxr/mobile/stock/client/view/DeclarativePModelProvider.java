/**
 * 
 */
package com.wxxr.mobile.stock.client.view;

import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author neillin
 *
 */
public class DeclarativePModelProvider {
	public static void updatePModel(IWorkbenchRTContext ctx){
		IWorkbenchManager mgr = ctx.getWorkbenchManager();
		mgr.registerView(new HomePageDescriptor());
		mgr.registerView(new LeftMenuViewDescriptor());
	}
}
