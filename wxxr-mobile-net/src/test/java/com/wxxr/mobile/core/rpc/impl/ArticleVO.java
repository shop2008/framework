package com.wxxr.mobile.core.rpc.impl;

import java.io.Serializable;
import java.util.Date;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ArticlesVo")
public class ArticleVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	 
	private String id; 
	
	private String title; 
	
	private int power ; 
	
	private String articleUrl;    	
	
	private String articleCreateDate; 
	
	private Date updateDate;
	
	private String source; 
	
	private String abstracts; 
	
	private String thumbnails; 
	
	private String thumbnailsInfo;  
	
	private String tag; 
	
	private String cdurl; 
	
	private String cdId;  
	
	private String content; 
	
	private int resultType;
	
	private String type;
	
	private String stock;   
	
	private int isAvailable;
	
	private int isNotice ;
	
	private String userName ; 
	
	
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

	
	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	
	public String getArticleUrl() {
		return articleUrl;
	}

	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}

	
	public String getArticleCreateDate() {
		return articleCreateDate;
	}

	public void setArticleCreateDate(String articleCreateDate) {
		this.articleCreateDate = articleCreateDate;
	}

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	
	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	
	public String getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(String thumbnails) {
		this.thumbnails = thumbnails;
	}
	
	
	public String getThumbnailsInfo() {
		return thumbnailsInfo;
	}

	public void setThumbnailsInfo(String thumbnailsInfo) {
		this.thumbnailsInfo = thumbnailsInfo;
	}

	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	
	public String getCdurl() {
		return cdurl;
	}

	public void setCdurl(String cdurl) {
		this.cdurl = cdurl;
	}

	
	public String getCdId() {
		return cdId;
	}

	public void setCdId(String cdId) {
		this.cdId = cdId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public int getResultType() {
		return resultType;
	}
    
	
	public void setResultType(int resultType) {
		this.resultType = resultType;
	}

	public int getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(int isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}
	public int getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(int isNotice) {
		this.isNotice = isNotice;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ArticleVO [id=" + id + ", title=" + title + ", power=" + power
				+ ", articleUrl=" + articleUrl + ", articleCreateDate="
				+ articleCreateDate + ", updateDate=" + updateDate
				+ ", source=" + source + ", abstracts=" + abstracts
				+ ", thumbnails=" + thumbnails + ", thumbnailsInfo="
				+ thumbnailsInfo + ", tag=" + tag + ", cdurl=" + cdurl
				+ ", cdId=" + cdId + ", content=" + content + ", resultType="
				+ resultType + ", type=" + type + ", stock=" + stock
				+ ", isAvailable=" + isAvailable + ", isNotice=" + isNotice
				+ ", userName=" + userName + "]";
	}
	
	
	
}
