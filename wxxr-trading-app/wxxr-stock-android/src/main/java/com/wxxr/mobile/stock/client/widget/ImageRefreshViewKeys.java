package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

public class ImageRefreshViewKeys {

	public static final AttributeKey<Double> totalMoneyProfit = new AttributeKey<Double>(Double.class, "totalMoneyProfit");
	public static final AttributeKey<Long> totalScoreProfit = new AttributeKey<Long>(Long.class, "totalScoreProfit");
	public static final AttributeKey<String> userHomeBackUri = new AttributeKey<String>(String.class, "userHomeBackUri");
	public static final AttributeKey<String> userIconUri = new AttributeKey<String>(String.class, "userIconUri");
	public static final AttributeKey<Long> joinShareCount = new AttributeKey<Long>(Long.class, "joinShareCount");
	public static final AttributeKey<Long> challengeShareCount = new AttributeKey<Long>(Long.class, "challengeShareCount");
	public static final AttributeKey<String> userId = new AttributeKey<String>(String.class, "userId");
	public static final AttributeKey<String> userName = new AttributeKey<String>(String.class, "userName");
	
	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(totalMoneyProfit);
		attrMgr.registerAttribute(totalScoreProfit);
		attrMgr.registerAttribute(userHomeBackUri);
		attrMgr.registerAttribute(userIconUri);
		attrMgr.registerAttribute(joinShareCount);
		attrMgr.registerAttribute(challengeShareCount);
		attrMgr.registerAttribute(userId);
		attrMgr.registerAttribute(userName);
	}
}
