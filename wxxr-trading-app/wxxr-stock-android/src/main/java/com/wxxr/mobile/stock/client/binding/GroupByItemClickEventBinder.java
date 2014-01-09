package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.ItemClickEventBinding;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IEventBinder;
import com.wxxr.mobile.core.ui.api.IEventBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

public class GroupByItemClickEventBinder implements IEventBinder {

	@SuppressWarnings("unused")
	private IWorkbenchRTContext context;
	
	@Override
	public IEventBinding createBinding(IBindingContext context,
			String fieldName, String cmdName, Map<String, String> attrs) {
		IAndroidBindingContext ctx = (IAndroidBindingContext)context;
		GroupByItemClickEventBinding binding = new GroupByItemClickEventBinding(ctx.getBindingControl(), cmdName, fieldName);
		binding.init(this.context);
		return binding;
	}

	@Override
	public void destory() {
		this.context = null;

	}

	@Override
	public void init(IWorkbenchRTContext mngCtx) {
		this.context = mngCtx;

	}

}
