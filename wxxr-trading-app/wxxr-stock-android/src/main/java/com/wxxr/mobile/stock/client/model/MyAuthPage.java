/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.PageBase;

/**
 * @author neillin
 *
 */
@View(name="myAuthPage")
@AndroidBinding(type=AndroidBindingType.ACTIVITY,layoutId="R.layout.price_center_page_layout")
public abstract class MyAuthPage extends PageBase {

}
