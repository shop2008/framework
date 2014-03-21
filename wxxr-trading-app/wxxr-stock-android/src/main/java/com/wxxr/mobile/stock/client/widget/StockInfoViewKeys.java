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
public abstract class StockInfoViewKeys {
	public static final AttributeKey<String> codeBean = new AttributeKey<String>(String.class, "codeBean");
	public static final AttributeKey<String> nameBean = new AttributeKey<String>(String.class, "nameBean");
	public static final AttributeKey<String> marketBean = new AttributeKey<String>(String.class, "marketBean");
	
	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(codeBean);
		attrMgr.registerAttribute(nameBean);
		attrMgr.registerAttribute(marketBean);
	}
}
