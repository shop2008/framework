/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
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
	public IBinding<IView> createBinding(IBindingContext context,
			IBindingDescriptor descriptor) {
		AndroidViewBinding androidViewBinding = new AndroidViewBinding(context,((IAndroidBindingDescriptor)descriptor).getBindingLayoutId());
		androidViewBinding.init(this.context);
		return androidViewBinding;
	}
	
}
	
