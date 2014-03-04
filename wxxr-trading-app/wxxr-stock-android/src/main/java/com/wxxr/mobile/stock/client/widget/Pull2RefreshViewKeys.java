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
public abstract class Pull2RefreshViewKeys {
	public static final AttributeKey<Boolean> enablePullDownRefresh = new AttributeKey<Boolean>(Boolean.class, "enablePullDownRefresh");
	public static final AttributeKey<Boolean> enablePullUpRefresh = new AttributeKey<Boolean>(Boolean.class, "enablePullUpRefresh");
	public static final AttributeKey<Boolean> noMoreDataAlert = new AttributeKey<Boolean>(Boolean.class, "noMoreDataAlert");
	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(enablePullDownRefresh);
		attrMgr.registerAttribute(enablePullUpRefresh);
		attrMgr.registerAttribute(noMoreDataAlert);
	}
}
