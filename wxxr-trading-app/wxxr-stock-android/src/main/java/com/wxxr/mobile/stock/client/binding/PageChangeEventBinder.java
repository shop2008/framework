/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IEventBinder;
import com.wxxr.mobile.core.ui.api.IEventBinding;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author dz
 *
 */
public class PageChangeEventBinder implements IEventBinder {

	@SuppressWarnings("unused")
	private IWorkbenchRTContext context;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.api.IEventBinder#createBinding(com.wxxr.mobile.core.ui.api.IBindingContext, java.lang.String, java.lang.String, java.util.Map)
	 */
	@Override
	public IEventBinding createBinding(IBindingContext context,
			String fieldName, String cmdName, Map<String, String> attrs) {
		IAndroidBindingContext ctx = (IAndroidBindingContext)context;
		return new PageChangeEventBinding(ctx.getBindingControl(), cmdName, fieldName);
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
