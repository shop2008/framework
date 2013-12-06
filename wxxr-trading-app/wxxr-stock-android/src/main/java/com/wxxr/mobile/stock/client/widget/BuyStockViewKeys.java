/**
 * 
 */
package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

/**
 * @author dz
 *
 */
public abstract class BuyStockViewKeys {
	public static final AttributeKey<String> marketPrice = new AttributeKey<String>(String.class, "marketPrice");
	public static final AttributeKey<String> orderPrice = new AttributeKey<String>(String.class, "orderPrice");
	public static final AttributeKey<String> fund = new AttributeKey<String>(String.class, "fund");
	public static final AttributeKey<String> sellCount = new AttributeKey<String>(String.class, "sellCount");
	public static final AttributeKey<String> type = new AttributeKey<String>(String.class, "type");
	
	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(marketPrice);
		attrMgr.registerAttribute(orderPrice);
		attrMgr.registerAttribute(fund);
		attrMgr.registerAttribute(sellCount);
		attrMgr.registerAttribute(type);
	}
}
