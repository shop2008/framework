package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="VirtualViewMoreView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.virtual_view_more_layout")
public abstract class VirtualViewMoreView extends ViewBase implements IModelUpdater {
	
	@Command
	String virtualViewMore(InputEvent event) {
		System.out.println("---virtualViewMore---");
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		// TODO Auto-generated method stub
		
	}
}
