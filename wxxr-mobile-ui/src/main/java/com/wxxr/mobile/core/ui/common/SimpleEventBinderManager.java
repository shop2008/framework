/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.core.ui.api.IEventBinder;
import com.wxxr.mobile.core.ui.api.IEventBinderManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;


/**
 * @author neillin
 *
 */
public class SimpleEventBinderManager implements IEventBinderManager {
	@SuppressWarnings("unused")
	private final IWorkbenchRTContext context;
	
	private Map<String, IEventBinder> binders = new HashMap<String, IEventBinder>();

	public SimpleEventBinderManager(IWorkbenchRTContext ctx){
		this.context = ctx;
	}


	@Override
	public IEventBinder getFieldBinder(String eventType) {
		return this.binders.get(eventType);
	}

	@Override
	public void registerFieldBinder(String eventType, IEventBinder binder) {
		this.binders.put(eventType, binder);
	}

	@Override
	public boolean unregisterFieldBinder(String eventType,IEventBinder binder) {
		IEventBinder b = this.binders.get(eventType);
		if(b == binder){
			this.binders.remove(eventType);
			return true;
		}
		return false;
	}

}
