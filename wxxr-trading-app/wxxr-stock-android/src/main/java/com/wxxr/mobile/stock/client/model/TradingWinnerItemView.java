/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.ViewBase;

/**
 * @author wangxuyang
 *
 */
@View(name="earnRankListItemView")
@AndroidBinding(type=AndroidBindingType.VIEW,layoutId="R.layout.layout_elv_earn_money_item")
public abstract class TradingWinnerItemView extends ViewBase implements IModelUpdater{
	@Field(valueKey="text")
	String text;
	DataField<String> textField;
	@Field(valueKey="imageURI")
	String imageUrl;
	DataField<String> imageUrlField;
	String linkUrl;
	
	@Override
	public void updateModel(Object value) {
		
		
	}
	
	@Command(description="点击文本事件")
	String handleTextClick(InputEvent event){
		return null;
	}
	@Command(description="点击图片事件")
	String handleImageClick(InputEvent event){
		return null;
	}
}
