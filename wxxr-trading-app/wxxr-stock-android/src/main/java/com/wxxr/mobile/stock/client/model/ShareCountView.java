package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.biz.ShareCountBean;


@View(name="ShareCountView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.share_count_layout")
public abstract class ShareCountView extends ViewBase implements IModelUpdater {

	@Field(valueKey="text", binding="${bean!=null?bean.shareCount:'--'}")
	String joinSharedNum;
	
	@Field(valueKey="text", binding="${(bean!=null&&bean.isVirtual()==true)?'参赛交易盘':'挑战交易盘'}")
	String label;
	
	@Bean
	ShareCountBean bean;
	
	@Override
	public void updateModel(Object value) {
		if(value instanceof ShareCountBean) {
			registerBean("bean", value);
		}
	}
}
