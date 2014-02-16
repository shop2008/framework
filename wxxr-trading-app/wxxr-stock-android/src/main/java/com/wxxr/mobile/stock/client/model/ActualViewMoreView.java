package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="ActualViewMoreView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.actual_view_more_layout")
public abstract class ActualViewMoreView extends ViewBase implements IModelUpdater {
	
	@Command
	String challengeViewMore(InputEvent event) {
		System.out.println("---challengeViewMore---");
		return null;
	}
	
	@Override
	public void updateModel(Object value) {
		// TODO Auto-generated method stub
		
	}
}
