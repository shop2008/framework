package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="NewsTitleItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.news_title_layout")
public abstract class NewsTitleItemView extends ViewBase implements IModelUpdater {

	@Bean
	String titleValue;
	
	@Field(valueKey="text", binding="${titleValue!=null?titleValue:null}",visibleWhen="${titleValue!=null}")
	String title;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof String) {
			this.titleValue = (String) value;
			registerBean("titleValue", this.titleValue);
		}
	}
}
