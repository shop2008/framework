package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;

@View(name="SystemNewsItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.system_news_item_layout")
public abstract class SystemNewsItemView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text", binding="${message.title}")
	String title;

	@Field(valueKey="text", binding="${message.content}")
	String content;
	
	@Field(valueKey="text", binding="${message.attrs.get('time')}")
	String time;
	
	@Bean
	RemindMessageBean message;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof RemindMessageBean) {
			registerBean("message", value);
			message = (RemindMessageBean)value;
		}
	}
}
