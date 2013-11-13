/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="StockBean")
public class Stock {
	private String code;
	private String name;
	private String pyCode;//拼音代码	
}
