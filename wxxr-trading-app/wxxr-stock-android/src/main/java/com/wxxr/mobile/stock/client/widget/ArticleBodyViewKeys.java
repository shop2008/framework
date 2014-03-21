package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

public abstract class ArticleBodyViewKeys {

	public static final AttributeKey<String> loadUrl = new AttributeKey<String>(String.class, "loadUrl");

	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(loadUrl);
	}
}
