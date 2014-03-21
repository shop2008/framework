package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.ClickEventBinding;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IEventBinder;
import com.wxxr.mobile.core.ui.api.IEventBinding;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

public class TextChangedEventBinder implements IEventBinder {

	@SuppressWarnings("unused")
	private IWorkbenchRTContext context;
	
	@Override
	public IEventBinding createBinding(IBindingContext context,
			String fieldName, String cmdName, Map<String, String> attrs) {
		IAndroidBindingContext ctx = (IAndroidBindingContext)context;
		return new TextChangedEventBinding(ctx.getBindingControl(), cmdName, fieldName);
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
