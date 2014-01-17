package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="InfoNoticeTitleView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.info_notice_title_layout")
public abstract class InfoNoticeTitleView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text", binding="${titleValue!=null?titleValue:null}", visibleWhen="${titleValue!=null}")
	String title;
	
	@Bean
	String titleValue;
	@Override
	public void updateModel(Object value) {
			
		if (value instanceof String) {
			
			this.titleValue = (String) value;
			registerBean("titleValue", this.titleValue);
		}
	}	
}
