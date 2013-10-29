/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import android.content.Context;
import android.view.View;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IFieldBinder;
import com.wxxr.mobile.core.ui.api.IFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author neillin
 *
 */
public class SimpleFieldBinder implements IFieldBinder {

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
		return new BasicFieldBinding((IAndroidBindingContext)context, fieldName, attrs);
	}

}
