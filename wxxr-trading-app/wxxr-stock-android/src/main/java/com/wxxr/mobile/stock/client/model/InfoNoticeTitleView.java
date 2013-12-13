package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.utils.StringTime2StringConvertor;

@View(name="InfoNoticeTitleView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.info_notice_title_layout")
public abstract class InfoNoticeTitleView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text", binding="${title}", converter="stime2StrConvertor")
	String title;

	@Convertor(
			params={@Parameter(name="format", value="yyyy-MM-dd"),
					@Parameter(name="nullString", value="--:--")
			}
			)
	
	StringTime2StringConvertor stime2StrConvertor;
	@Override
	public void updateModel(Object value) {
			
		if (value instanceof String) {
			registerBean("title", value);
		}
	}	
}
