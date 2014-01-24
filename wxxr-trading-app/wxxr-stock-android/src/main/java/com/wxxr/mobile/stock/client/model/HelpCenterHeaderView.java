package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;


@View(name="HelpCenterHeaderView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.help_list_header_view_layout")
public abstract class HelpCenterHeaderView extends ViewBase implements IModelUpdater {

	@Override
	public void updateModel(Object value) {
		
	}

}
