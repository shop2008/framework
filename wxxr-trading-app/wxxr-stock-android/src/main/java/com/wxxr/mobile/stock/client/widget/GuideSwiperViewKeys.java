package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

public class GuideSwiperViewKeys {
	public static final AttributeKey<Integer> position = new AttributeKey<Integer>(Integer.class, "position");

	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(position);
	}
}
