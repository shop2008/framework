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
	int BING_ATTRS_TAG_ID = 0x7fffffff; //0x7f08003a
	String BINDING_NAMESPACE = "http://wxxr.com.cn/binding";
	String BINDING_FIELD_NAME = "field";
	String BINDING_DECORATOR_NAME = "decorator";
}
