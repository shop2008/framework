package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;


import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;


@View(name="guidePage")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.guide_page_layout")
public abstract class GuidePage extends PageBase {

	@Field(valueKey="options", binding="${imageUris}")
	List<String> guideImages;
	
	
	int selectPos;
	
	@OnShow
	protected void initGuideImages() {
		List<String> guideImages = new ArrayList<String>();
		for(int i=0;i<4;i++) {
			guideImages.add("resourceId:drawable/guide"+(i+1));
		}
		registerBean("imageUris", guideImages);
	}
	
	@Command
	String tryClick(InputEvent event) {
		
		if (event.getEventType().equals(InputEvent.EVENT_TYPE_CLICK)) {
			if (selectPos == 3) {
				getUIContext().getWorkbenchManager().getPageNavigator().hidePage(this);
			}
		}
		return null;
	}

	@Command
	String selected(InputEvent event) {
		
		Integer position = (Integer)event.getProperty("selectPos");
		
		if (position != null) {
			selectPos = position;
			
			System.out.println("--------select position----------------"+selectPos);
		}
		return null;
	}
}
