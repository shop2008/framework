package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;

@View(name = "helpCenterItemView")
@AndroidBinding(type = AndroidBindingType.VIEW, layoutId = "R.layout.help_center_page_layout_item")
public abstract class HelpCenterItemView extends ViewBase implements
		IModelUpdater {

	@Bean
	ArticleBean article;
	
	@Field(valueKey = "text",binding="${article!=null?article.title:'--'}")
	String title;
	
	@Field(valueKey = "text",binding="${article!=null?article.abstractInfo:'--'}")
	String abstractInfo;
	
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof ArticleBean) {
			ArticleBean article = (ArticleBean) data;
			registerBean("article", article);
		}
	}
}
