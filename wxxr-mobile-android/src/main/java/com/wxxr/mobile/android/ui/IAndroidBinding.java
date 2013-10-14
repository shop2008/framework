/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;

/**
 * @author neillin
 *
 */
public interface IAndroidBinding<M extends IUIComponent> extends IBinding<M> {
	String BINDING_NAMESPACE = "http://wxxr.com.cn/binding";
	String BINDING_FIELD_NAME = "field";
}
