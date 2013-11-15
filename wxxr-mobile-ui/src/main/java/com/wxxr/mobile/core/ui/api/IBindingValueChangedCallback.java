/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import com.wxxr.mobile.core.ui.common.UIComponent;

/**
 * @author neillin
 *
 */
public interface IBindingValueChangedCallback {
	void valueChanged(UIComponent component, AttributeKey<?>... keys);
}
