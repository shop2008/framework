/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.view.View;

import com.wxxr.mobile.core.ui.api.IUIComponent;

/**
 * @author neillin
 *
 */
public interface IFieldBindingFactory {
	String getName();
	IFieldBinding createBinding(IBindingContext context, View pField, IUIComponent lField);
}
