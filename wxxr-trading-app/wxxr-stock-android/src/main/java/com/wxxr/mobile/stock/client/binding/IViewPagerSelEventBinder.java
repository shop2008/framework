package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IEventBinder;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

public class IViewPagerSelEventBinder implements IEventBinder {

	
	@SuppressWarnings("unused")
	private IWorkbenchRTContext context;
	
	
	
	@Override
	public IBinding<IView> createBinding(IBindingContext context,
			String fieldName, String cmdName, Map<String, String> attrs) {
		IAndroidBindingContext ctx = (IAndroidBindingContext)context;
		return new IViewPagerSelEventBinding(ctx.getBindingControl(), cmdName, fieldName);
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
