/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.core.ui.api.IUIComponent;

import android.view.View;

/**
 * @author neillin
 *
 */
public interface IFieldBindingFactory {
	String getName();
	IFieldBinding createBinding(IBindingContext context, View pField, IUIComponent lField);
}
