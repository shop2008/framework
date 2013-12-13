/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IEventBinder;
import com.wxxr.mobile.core.ui.api.IEventBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author neillin
 *
 */
public class ItemClickEventBinder implements IEventBinder {

	@SuppressWarnings("unused")
	private IWorkbenchRTContext context;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IEventBinder#createBinding(com.wxxr.mobile.core.ui.api.IBindingContext, java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public IEventBinding createBinding(IBindingContext context,
			String fieldName, String cmdName, Map<String, String> attrs) {
		IAndroidBindingContext ctx = (IAndroidBindingContext)context;
		ItemClickEventBinding binding = new ItemClickEventBinding(ctx.getBindingControl(), cmdName, fieldName);
		binding.init(this.context);
		return binding;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IEventBinder#init(com.wxxr.mobile.core.ui.api.IWorkbenchRTContext)
	 */
	@Override
	public void init(IWorkbenchRTContext mngCtx) {
		this.context = mngCtx;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IEventBinder#destory()
	 */
	@Override
	public void destory() {
		this.context = null;
	}

}
