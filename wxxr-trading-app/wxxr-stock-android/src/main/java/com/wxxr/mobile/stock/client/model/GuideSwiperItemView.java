package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="guideSwiperItemView")
@AndroidBinding(type=AndroidBindingType.FRAGMENT_ACTIVITY, layoutId="R.layout.guide_swiper_item_layout")
public abstract class GuideSwiperItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey="imageURI", binding="${imagUri}")
	String selImage;
	
	
	@Override
	public void updateModel(Object value) {
		
		if (value instanceof String) {
			
			registerBean("imagUri", value);
		}
	}
	
}
