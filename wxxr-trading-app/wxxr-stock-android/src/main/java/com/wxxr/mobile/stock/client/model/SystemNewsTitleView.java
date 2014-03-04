package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="SystemNewsTitleView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.system_news_title_layout")
public abstract class SystemNewsTitleView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text", binding="${titleValue!=null?titleValue:null}")
	String title;
	
	@Bean
	String titleValue;
	@Override
	public void updateModel(Object value) {
		if(value instanceof String) {
			this.titleValue = (String) value;
			registerBean("titleValue", titleValue);
		}
	}
}
