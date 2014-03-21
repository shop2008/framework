/**
 * 
 */
package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;


/**
 * 推送消息
 * @author wangyan
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="PullMessageBean")
public class PullMessage {

	private Long id;
	private String createDate;
	private String title;
	private String message;
	private String articleUrl;
	private String phone;
	
	
}
