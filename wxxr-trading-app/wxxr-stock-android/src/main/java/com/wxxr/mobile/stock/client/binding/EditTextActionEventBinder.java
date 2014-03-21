package com.wxxr.mobile.stock.client.binding;

import java.util.Map;


import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IEventBinder;
import com.wxxr.mobile.core.ui.api.IEventBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

public class EditTextActionEventBinder implements IEventBinder {

	@SuppressWarnings("unused")
	private IWorkbenchRTContext context;
	
	@Override
	public IEventBinding createBinding(IBindingContext context,
			String fieldName, String cmdName, Map<String, String> attrs) {
		
		IAndroidBindingContext ctx = (IAndroidBindingContext)context;
		return new EditTextActionEventBinding(ctx.getBindingControl(), cmdName, fieldName);
	}
	
	@Override
	public void init(IWorkbenchRTContext mngCtx) {
		this.context = mngCtx;

	}

	@Override
	public void destory() {
		this.context = null;
	}

}
