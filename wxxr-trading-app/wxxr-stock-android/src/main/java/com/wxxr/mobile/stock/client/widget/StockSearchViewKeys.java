package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

public abstract class StockSearchViewKeys {

	public static final AttributeKey<Boolean> keyBoardViewVisible = new AttributeKey<Boolean>(Boolean.class, "keyBoardViewVisible");
	public static final AttributeKey<Boolean> keyBoardShow = new AttributeKey<Boolean>(Boolean.class, "keyBoardShow");
	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(keyBoardViewVisible);
		attrMgr.registerAttribute(keyBoardShow);
	}
}
