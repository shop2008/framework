package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

public abstract class FocusAttributeKeys {
	
	public static final AttributeKey<Boolean> focusable = new AttributeKey<Boolean>(Boolean.class, "focusable");
	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(focusable);
	}
}
