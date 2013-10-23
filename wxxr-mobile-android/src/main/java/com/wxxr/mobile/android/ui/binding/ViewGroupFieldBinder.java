/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IFieldBinder;
import com.wxxr.mobile.core.ui.api.IUIComponent;
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
	public IBinding<IUIComponent> createBinding(IBindingContext context,String fieldName,
			Map<String, String> attrs) {
		return new ViewGroupFieldBinding((IAndroidBindingContext)context, fieldName, attrs);
	}

}
