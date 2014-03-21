/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

import java.util.Map;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * @author wangyan
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="RemindMessageBean")
public  class RemindMessage {
	
	protected String id;// 提醒ID
	protected String type;// 提醒类型
	protected String title;//标题
	protected String content;// 提醒内容
	protected String createdDate;
	protected String acctId;
	protected Map<String, String> attrs;
}
