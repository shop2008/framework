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
	
	@Bean
	ArticleBean article;
	
	@Field(valueKey = "text",binding="${article!=null?article.title:'--'}")
	String title;
	
	@Field(valueKey = "text",binding="${article!=null?article.title:'--'}")
	String title1;
	
	@Field(valueKey = "text",binding="${article!=null?article.abstractInfo:'--'}")
	String abstractInfo;	
	
	@Field(valueKey="text" ,attributes={
			@Attribute(name = "background", value = "${background!=null?background:null}")
	})
	String backgroundColor;
	
	@Field(valueKey="imageURI",binding="${(article!=null && article.imageUrl!=null && index==0)?article.imageUrl:null}")
	String imgUrl;
	
	@Field(valueKey="visible",visibleWhen="${index==0}")
	boolean imgItem;
	
	@Field(valueKey="visible",visibleWhen="${index==1}")
	boolean textItem;
	
	@Bean
	int index = 0;
	
	@OnShow
	void setBackgroundColor(){
		Integer position = (Integer) getProperty("_item_position");
		if(position!=null){
			if(position==0){
				this.index = position;
			}else{
				this.index = 1;
				if(position % 2 ==0){
					registerBean("background", "resourceId:drawable/listview_selected_item2");
				}else{
					registerBean("background", "resourceId:drawable/listview_selected_item");
				}
			}
			registerBean("index",this.index);
		}
	}
	
	
	@Override
	public void updateModel(Object data) {
		if (data instanceof ArticleBean) {
			ArticleBean article = (ArticleBean) data;
			registerBean("article", article);
		}
	}
}
