package com.wxxr.mobile.stock.client.widget;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;

public abstract class MinuteLineViewKeys {

	/**边框颜色 默认红色*/
	public static final AttributeKey<String> stockBorderColor= new AttributeKey<String>(String.class, "stockBorderColor");
	/**上涨颜色 默认红色*/
	public static final AttributeKey<String> stockUpColor= new AttributeKey<String>(String.class, "stockUpColor");
	/**下跌颜色 默认绿色*/
	public static final AttributeKey<String> stockDownColor= new AttributeKey<String>(String.class, "stockDownColor");
	/**平均线颜色 默认黄色*/
	public static final AttributeKey<String> stockAverageLineColor= new AttributeKey<String>(String.class, "stockAverageLineColor");
	/**昨收价 默认白or黑色*/
	public static final AttributeKey<String> stockCloseColor= new AttributeKey<String>(String.class, "stockCloseColor");
	
	public static final AttributeKey<String> background= new AttributeKey<String>(String.class, "background");
	
	public static final AttributeKey<String> stockCode = new AttributeKey<String>(String.class, "stockCode"); //股票代码
	public static final AttributeKey<String> stockClose = new AttributeKey<String>(String.class, "stockClose"); //昨收价
	public static final AttributeKey<String> stockDate = new AttributeKey<String>(String.class, "stockDate"); //日期
	public static final AttributeKey<String> stockType = new AttributeKey<String>(String.class, "stockType"); //股票类型0-指数；1-个股
	
	public static final AttributeKey<Integer> count = new AttributeKey<Integer>(Integer.class, "count");
	public static final AttributeKey<Integer> position = new AttributeKey<Integer>(Integer.class, "position");
	
	public static void registerKeys(IFieldAttributeManager attrMgr){
		attrMgr.registerAttribute(stockBorderColor);
		attrMgr.registerAttribute(stockUpColor);
		attrMgr.registerAttribute(stockDownColor);
		attrMgr.registerAttribute(stockAverageLineColor);
		attrMgr.registerAttribute(stockCloseColor);
		attrMgr.registerAttribute(stockCode);
		attrMgr.registerAttribute(stockClose);
		attrMgr.registerAttribute(stockDate);
		attrMgr.registerAttribute(stockType);
		attrMgr.registerAttribute(background);
		attrMgr.registerAttribute(count);
		attrMgr.registerAttribute(position);
	}
}
