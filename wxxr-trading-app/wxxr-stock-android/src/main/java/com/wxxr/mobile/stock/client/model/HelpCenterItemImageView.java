package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;

@View(name = "helpCenterItemImageView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.help_center_page_image_layout_item")
public abstract class HelpCenterItemImageView extends ViewBase implements IModelUpdater {

	static Trace log = Trace.getLogger(HelpCenterItemImageView.class);
	
	@Bean
	ArticleBean article;
	
	@Field(valueKey = "text",binding="${article!=null?article.title:'--'}")
	String title;
	
	@Field(valueKey="imageURI",binding="${(article!=null && article.imageUrl!=null)?article.imageUrl:'--'}")
	String imgUrl;
	
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof ArticleBean) {
			registerBean("article", data);
		}
	}
}
