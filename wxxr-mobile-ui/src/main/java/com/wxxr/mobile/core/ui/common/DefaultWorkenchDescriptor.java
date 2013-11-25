/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author neillin
 *
 */
public class DefaultWorkenchDescriptor implements IWorkbenchDescriptor {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchDescriptor#getTitle()
	 */
	@Override
	public String getTitle() {
		return "Default Workbench";
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchDescriptor#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Default Workbench";
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchDescriptor#getExceptionHandlers()
	 */
	@Override
	public INavigationDescriptor[] getDefaultNavigations() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IWorkbenchDescriptor#createWorkbench(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	@Override
	public IWorkbench createWorkbench(IWorkbenchRTContext ctx) {
		return new WorkbenchBase(ctx) {
		};
	}

}
