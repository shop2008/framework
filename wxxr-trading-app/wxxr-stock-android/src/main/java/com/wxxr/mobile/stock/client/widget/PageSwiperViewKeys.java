package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

public abstract class PageSwiperViewKeys {

	public static final AttributeKey<Boolean> titleBarVisible = new AttributeKey<Boolean>(Boolean.class, "titleBarVisible");
	
	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(titleBarVisible);
	}
}
