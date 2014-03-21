/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;

/**
 * @author neillin
 * 
 */
@View(name = "rootView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.root_view_layout")
public abstract class RootView extends ViewBase {

}
