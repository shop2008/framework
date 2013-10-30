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
@View(name="helpCenter")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.help_center_page_layout")
public abstract class HelpCenterView extends ViewBase {

}
