package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

public class ImageRefreshViewKeys {

	public static final AttributeKey<String> totalMoneyProfit = new AttributeKey<String>(String.class, "totalMoneyProfit");
	public static final AttributeKey<String> totalScoreProfit = new AttributeKey<String>(String.class, "totalScoreProfit");
	public static final AttributeKey<String> userHomeBackUri = new AttributeKey<String>(String.class, "userHomeBackUri");
	public static final AttributeKey<String> userIconUri = new AttributeKey<String>(String.class, "userIconUri");
	
	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(totalMoneyProfit);
		attrMgr.registerAttribute(totalScoreProfit);
		attrMgr.registerAttribute(userHomeBackUri);
		attrMgr.registerAttribute(userIconUri);
	}
}
