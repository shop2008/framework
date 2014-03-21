package com.wxxr.mobile.stock.app.model;

import com.wxxr.mobile.core.annotation.BindableBean;

@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="MessageInfoBean")
public class MessageInfo {
	private String title; //消息标题
	private String content; //消息内容
	private long date; //消息日期
}
