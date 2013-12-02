package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnShow;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;

@View(name = "helpCenterItemTextView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.help_center_page_layout_item")
public abstract class HelpCenterItemTextView extends ViewBase implements IModelUpdater {

	static Trace log = Trace.getLogger(HelpCenterItemTextView.class);
	
	@Bean
	String background;
	
	@Field(valueKey="text" ,attributes={
			@Attribute(name = "background", value = "${background!=null?background:null}")
	})
	String backgroundColor;
	
	
	@OnShow
	void setBackgroundColor(){
		Integer position = (Integer) getProperty("_item_position");
		if(position!=null){
			if(position % 2 ==0){
				registerBean("background", "resourceId:drawable/listview_selected_item2");
			}else{
				registerBean("background", "resourceId:drawable/listview_selected_item");
			}
		}
	}
	
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
