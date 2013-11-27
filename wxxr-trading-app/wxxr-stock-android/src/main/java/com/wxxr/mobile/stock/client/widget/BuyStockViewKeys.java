/**
 * 
 */
package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

/**
 * @author neillin
 *
 */
public abstract class BuyStockViewKeys {
	public static final AttributeKey<String> marketPrice = new AttributeKey<String>(String.class, "marketPrice");
	public static final AttributeKey<String> orderPrice = new AttributeKey<String>(String.class, "orderPrice");
	public static final AttributeKey<String> fund = new AttributeKey<String>(String.class, "fund");
	
	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(marketPrice);
		attrMgr.registerAttribute(orderPrice);
		attrMgr.registerAttribute(fund);
	}
}
