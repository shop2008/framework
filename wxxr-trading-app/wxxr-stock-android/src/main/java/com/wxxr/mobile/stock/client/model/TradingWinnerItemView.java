/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.client.bean.EarnRankItem;

/**
 * @author wangxuyang
 *
 */
@View(name="earnRankListItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.earn_money_rank_layout_item")
public abstract class TradingWinnerItemView extends ViewBase implements IModelUpdater{
	@Field(valueKey="text")
	String text;
	DataField<String> textField;
	
	@Field(valueKey="imageURI")
	String imageUrl;
	DataField<String> imageUrlField;
	
	String linkUrl;
	int postion = 0;
	
	@Field(valueKey="enabled")
	boolean isClose;
	DataField<Boolean> isCloseField;
	
	@Override
	public void updateModel(Object value) {
		
		if(value instanceof EarnRankItem){
			EarnRankItem data = (EarnRankItem) value;
			this.text = data.getTitle();
			this.textField.setValue(this.text);
			this.imageUrl = data.getImgUrl();
			this.imageUrlField.setValue(this.imageUrl);
			this.imageUrlField.setAttribute(AttributeKeys.visible, true);
			this.isClose = true;
			this.isCloseField.setValue(this.isClose);
		}
		postion++;
	}
	
	@Command(description="点击文本事件")
	String showItemView(InputEvent event){
		boolean isOpen = this.imageUrlField.getAttribute(AttributeKeys.visible);
		if(!isOpen){
			this.imageUrlField.setAttribute(AttributeKeys.visible, true);
			this.isClose = true;
			this.isCloseField.setValue(this.isClose);
		}else{
			this.imageUrlField.setAttribute(AttributeKeys.visible, false);
			this.isClose = false;
			this.isCloseField.setValue(this.isClose);
		}
		return null;
	}
	@Command(description="点击图片事件")
	String handleImageClick(InputEvent event){
		return null;
	}
}
