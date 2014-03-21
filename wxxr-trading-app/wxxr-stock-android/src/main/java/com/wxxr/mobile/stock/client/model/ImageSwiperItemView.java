package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.CommandResult;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;

@View(name="ImageSwiperItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.image_swiper_view_item")
public abstract class ImageSwiperItemView extends ViewBase implements IModelUpdater {

	@Bean
	ArticleBean article;
	
	@Field(valueKey="backgroundImageURI",binding="${article!=null?article.imageUrl:'--'}")
	String swiperImage;
	
	  
	String articleUrl;
	
	
	
	@Command(navigations={@Navigation(on="web",showPage="webPage")})
	CommandResult linkItemClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType()) && articleUrl!=null){
			CommandResult result = new CommandResult();
			result.setResult("web");
			result.setPayload(articleUrl);
			return result;
		}
		return null;
	}
	
	

	@Override
	public void updateModel(Object data) {
		if(data instanceof ArticleBean){
			article = (ArticleBean)data;
			registerBean("article", data);
			this.articleUrl = article.getArticleUrl();
		}
	}
}
