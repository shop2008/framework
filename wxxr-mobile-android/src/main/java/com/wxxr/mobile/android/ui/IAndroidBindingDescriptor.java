/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.core.ui.api.IBindingDescriptor;

/**
 * @author neillin
 *
 */
public interface IAndroidBindingDescriptor extends IBindingDescriptor {
	Class<?> getTargetClass();
	int getBindingLayoutId();
	AndroidBindingType getBindingType();
	boolean hasToolbar();
}
