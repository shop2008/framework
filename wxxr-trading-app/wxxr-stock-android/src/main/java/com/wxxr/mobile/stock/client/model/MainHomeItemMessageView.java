package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.v2.bean.MessageMenuItem;

@View(name = "MainHomeItemMessageView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.main_home_item_message_view")
public abstract class MainHomeItemMessageView extends ViewBase implements
		IModelUpdater {

	@Bean
	MessageMenuItem messageBean;

	@Field(valueKey = "imageURI", binding = "${'resourceId:drawable/home_message'}${(messageBean!=null&&messageBean.type!=null)?messageBean.type:''}")
	String icon;

	@Field(valueKey = "text", binding = "${(messageBean!=null&&messageBean.num!=null)?messageBean.num:''}", visibleWhen = "${messageBean!=null&&messageBean.num!=null&&messageBean.num>0}")
	String number;

	@Field(valueKey = "text", binding = "${messageBean!=null?messageBean.title:'--'}")
	String title;

	@Field(valueKey = "text", binding = "${messageBean!=null?messageBean.date:'--'}")
	String date;

	@Field(valueKey = "text", binding = "${messageBean!=null?messageBean.message:'--'}", attributes = { @Attribute(name = "textColor", value = "${messageBean==null?'resourceId:color/gray':messageBean.type=='63'?'resourceId:color/yellow':'resourceId:color/gray'}") })
	String message;

	@Override
	public void updateModel(Object value) {
		if (value instanceof MessageMenuItem) {
			messageBean = (MessageMenuItem) value;
			registerBean("messageBean", value);
		}
	}
}
