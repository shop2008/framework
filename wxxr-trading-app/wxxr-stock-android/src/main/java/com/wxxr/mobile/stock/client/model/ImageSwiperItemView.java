package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Navigation;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;

@View(name="ImageSwiperItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.image_swiper_view_item")
public abstract class ImageSwiperItemView extends ViewBase implements IModelUpdater {
	
	@Field(valueKey="imageURI")
	String swiperImage;
	
	@Field(valueKey="text")
	String swiperTitle;
	
	DataField<String> swiperImageField;
	
	DataField<String> swiperImageLabelField;
	
	@Command(description="",commandName="linkItemClick")
	String linkItemClick(InputEvent event){
		if(InputEvent.EVENT_TYPE_CLICK.equals(event.getEventType())){
			//
		}
		return null;
	}

	protected String getSwiperImage() {
		return swiperImage;
	}

	protected void setSwiperImage(String swiperImage) {
		this.swiperImage = swiperImage;
		this.swiperImageLabelField.setValue(swiperImage);
	}

	protected String getSwiperTitle() {
		return swiperTitle;
	}

	protected void setSwiperTitle(String swiperTitle) {
		this.swiperTitle = swiperTitle;
		this.swiperImageLabelField.setValue(swiperTitle);
	}

	@Override
	public void updateModel(Object arg0) {
		
	}
}
