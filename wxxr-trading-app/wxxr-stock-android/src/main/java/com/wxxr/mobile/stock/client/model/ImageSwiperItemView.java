package com.wxxr.mobile.stock.client.model;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.Article;

@View(name="ImageSwiperItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.image_swiper_view_item")
public abstract class ImageSwiperItemView extends ViewBase implements IModelUpdater {
	

	@Field(valueKey="imageURI")
	String swiperImage;
	
	@Field(valueKey="text")
	String swiperTitle;
	
	String articleUrl;
	
	DataField<String> swiperImageField;
	
	DataField<String> swiperTitleField;
	
	
	@Command
	String linkItemClick(InputEvent event){
		Map<String, Object> tmap = new HashMap<String, Object>();
		tmap.put("webUrl", articleUrl);
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			IPage page = getUIContext().getWorkbenchManager().getWorkbench().getPage("webPage");
			getUIContext().getWorkbenchManager().getPageNavigator().showPage(page, tmap, null);
		}
		return null;
	}
	
	

	protected String getSwiperImage() {
		return swiperImage;
	}

	protected void setSwiperImage(String swiperImage) {
		this.swiperImage = swiperImage;
		this.swiperTitleField.setValue(swiperImage);
	}

	protected String getSwiperTitle() {
		return swiperTitle;
	}

	protected void setSwiperTitle(String swiperTitle) {
		this.swiperTitle = swiperTitle;
		this.swiperTitleField.setValue(swiperTitle);
	}

	@Override
	public void updateModel(Object data) {
		if(data instanceof Article){
			Article article = (Article)data;
			this.swiperImage = article.getImageUrl();
			this.swiperImageField.setValue(this.swiperImage);
			this.swiperTitle = article.getTitle();
			this.swiperTitleField.setValue(this.swiperTitle);
			this.articleUrl = article.getArticleUrl();
		}
	}
}
