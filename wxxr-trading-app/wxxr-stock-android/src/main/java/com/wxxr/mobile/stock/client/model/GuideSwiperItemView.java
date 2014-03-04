package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="GuideSwiperItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.guide_swiper_item_layout")
public abstract class GuideSwiperItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text",attributes={
			@Attribute(name = "imgUrl", value = "${imagUri!=null?imagUri:null}")
	})
	String selImage;
	
	
	@Override
	public void updateModel(Object value) {
		
		if (value instanceof String) {
			
			registerBean("imagUri", value);
		}
	}
	
}
