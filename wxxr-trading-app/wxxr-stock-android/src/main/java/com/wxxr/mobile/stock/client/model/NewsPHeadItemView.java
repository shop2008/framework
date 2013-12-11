package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;

@View(name="NewsPHeadItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.header_layout")
public abstract class NewsPHeadItemView extends ViewBase implements IModelUpdater {

	@Bean
	String title;
	
	@Field(valueKey="text", binding="${title}")
	String label;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof String) {
			registerBean("title", value);
		}
	}
}
