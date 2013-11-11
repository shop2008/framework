/**
 * 
 */
package com.wxxr.mobile.stock.client.bean;

/**
 * @author wangxuyang
 *
 */
public class Article {
	private String id;//文章 Id
	private String title; //文章标题
	private String abstractInfo;//摘要信息 
	private String articleUrl;//文章内容url
	private String imageUrl;//图片URL
	private String createDate;//创建日期
	private int power;//权重 
	private int type ;//文章类型 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAbstractInfo() {
		return abstractInfo;
	}
	public void setAbstractInfo(String abstractInfo) {
		this.abstractInfo = abstractInfo;
	}
	public String getArticleUrl() {
		return articleUrl;
	}
	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", abstractInfo="
				+ abstractInfo + ", articleUrl=" + articleUrl + ", imageUrl="
				+ imageUrl + ", createDate=" + createDate + ", power=" + power
				+ ", type=" + type + "]";
	}
	
	

}
