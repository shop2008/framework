package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IFieldBinder;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

public class GuideSwiperViewFieldBinder implements IFieldBinder {

	private IWorkbenchRTContext mngContext;
	@Override
	public IFieldBinding createBinding(IBindingContext context,
			String fieldName, Map<String, String> attrs) {
		
		return new GuideSwiperViewFieldBinding((IAndroidBindingContext)context, fieldName, attrs);
	}

	@Override
	public void init(IWorkbenchRTContext mngCtx) {
		this.mngContext = mngCtx;

	}

	@Override
	public void destory() {
		this.mngContext = null;
	}

}
