package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.service.IUserManagementService;

@View(name="InfoNoticeItemView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.notice_info_item_layout")
public abstract class InfoNoticeItemView extends ViewBase implements IModelUpdater {

	@Bean
	PullMessageBean message;
	
	@Field(valueKey="visible", binding="${!read}")
	boolean remindReaded;
	
	@Field(valueKey="text", binding="${message.createDate}")
	String date;
	
	@Field(valueKey="text", binding="${message.title}")
	String title;
	
	@Field(valueKey="text", binding="${message.message}")
	String content;
	
	@Override
	public void updateModel(Object value) {
		if (value instanceof PullMessageBean) {
			registerBean("message", value);
		}
	}
}
