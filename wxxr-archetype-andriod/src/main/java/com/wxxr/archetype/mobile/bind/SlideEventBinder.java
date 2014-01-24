/**
 * 
 */
package com.wxxr.archetype.mobile.bind;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IEventBinder;
import com.wxxr.mobile.core.ui.api.IEventBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author fudapeng
 *
 */
public class SlideEventBinder implements IEventBinder{

	@SuppressWarnings("unused")
	private IWorkbenchRTContext context;
	@Override
	public IEventBinding createBinding(IBindingContext arg0, String fieldName, String cmdName, Map<String, String> attrs) {
		IAndroidBindingContext ctx = (IAndroidBindingContext)arg0;
		return new SlideEventBinding(ctx.getBindingControl(), cmdName, fieldName);
	}

	@Override
	public void destory() {
		this.context = null;
		
	}

	@Override
	public void init(IWorkbenchRTContext arg0) {
		context = arg0;
	}

}
