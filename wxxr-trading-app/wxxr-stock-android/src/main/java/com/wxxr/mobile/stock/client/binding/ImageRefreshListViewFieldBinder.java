package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IFieldBinder;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

public class ImageRefreshListViewFieldBinder implements IFieldBinder {

	
	private IWorkbenchRTContext mngContext;
	
	@Override
	public IFieldBinding createBinding(IBindingContext context,
			String fieldName, Map<String, String> attrs) {
		// TODO Auto-generated method stub
		return null;
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
