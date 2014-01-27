package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="SystemNewsItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.system_news_item_layout")
public abstract class SytemNewsItemView extends ViewBase implements IModelUpdater {

	/**消息标题*/
	@Field(valueKey="text")
	String title;
	
	/**时间*/
	@Field(valueKey="text")
	String time;
	
	/**消息内容*/
	@Field(valueKey="text")
	String content;
	
	@Override
	public void updateModel(Object value) {
		// TODO Auto-generated method stub
		
	}
}
