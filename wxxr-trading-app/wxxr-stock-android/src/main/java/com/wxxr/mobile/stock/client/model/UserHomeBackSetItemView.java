package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="imageItemForHomeBack")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.user_home_set_item_layout")
public abstract class UserHomeBackSetItemView extends ViewBase implements IModelUpdater{

	@Field(valueKey="imageURI", binding="${selPic}")
	String userHomeBack;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof String) {
			registerBean("selPic", value);
		}
	}

}
