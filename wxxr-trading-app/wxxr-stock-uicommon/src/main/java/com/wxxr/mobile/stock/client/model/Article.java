/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.core.annotation.BindableBean;

/**
 * @author wangxuyang
 *
 */
@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="ArticleBean")
public class Article {
	private String id;//文章 Id
	private String title; //文章标题
	private String abstractInfo;//摘要信息 
	private String articleUrl;//文章内容url
	private String imageUrl;//图片URL
	private String createDate;//创建日期
	private int power;//权重 
	private int type ;//文章类型 
}
