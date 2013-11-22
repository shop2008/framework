/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author neillin
 *
 */
public class SimpleAndroidViewBinder implements IViewBinder {
	private final IWorkbenchRTContext context;
	
	public SimpleAndroidViewBinder(IWorkbenchRTContext ctx){
		this.context = ctx;
	}
	
	@Override
	public IViewBinding createBinding(IBindingContext context,
			IBindingDescriptor descriptor) {
		AndroidViewBinding androidViewBinding = new AndroidViewBinding(context,((IAndroidBindingDescriptor)descriptor).getBindingLayoutId(),descriptor.getBindingViewId());
		androidViewBinding.init(this.context);
		return androidViewBinding;
	}
	
}
	
