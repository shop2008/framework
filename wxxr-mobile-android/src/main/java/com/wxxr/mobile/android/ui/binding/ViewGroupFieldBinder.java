/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IFieldBinder;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author neillin
 *
 */
public class ViewGroupFieldBinder implements IFieldBinder {

	private IWorkbenchRTContext mngContext;
	
	@Override
	public void init(IWorkbenchRTContext mngCtx) {
		this.mngContext = mngCtx;
	}

	@Override
	public void destory() {
		this.mngContext = null;
	}

	@Override
	public IFieldBinding createBinding(IBindingContext context,String fieldName,
			Map<String, String> attrs) {
		ViewGroupFieldBinding binding = new ViewGroupFieldBinding((IAndroidBindingContext)context, fieldName, attrs);
		binding.init(this.mngContext);
		return binding;
	}

}
