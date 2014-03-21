package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="Guide1")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.guide_layout1")
public class Guide1 extends ViewBase implements IModelUpdater {

	@Override
	public void updateModel(Object arg0) {

	}

}
