/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import com.wxxr.mobile.core.event.api.GenericEventObject;
import com.wxxr.mobile.core.event.api.IBroadcastEvent;

/**
 * @author neillin
 *
 */
public class UISystemInitEvent extends GenericEventObject implements IBroadcastEvent{
	private final IWorkbenchRTContext context;
	
	public UISystemInitEvent(IWorkbenchRTContext ctx){
		this.context = ctx;
		setNeedSyncProcessed(true);
	}

	/**
	 * @return the context
	 */
	public IWorkbenchRTContext getContext() {
		return context;
	}
}
