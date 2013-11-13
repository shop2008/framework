package com.wxxr.stock.article.ejb.api;

import java.io.Serializable;
import java.util.Date;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ArticlesVo")
public class ArticleVO implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "id")
	private String id; // 文章id
	@XmlElement(name = "title")
	private String title; // 文章题目
	@XmlElement(name = "power")
	private int power; // 权重
	@XmlElement(name = "articleUrl")
	private String articleUrl; // 文章URL(相对地址)
	@XmlElement(name = "articleCreateDate")
	private String articleCreateDate; // 创建时间
	@XmlElement(name = "updateDate")
	private Date updateDate;
	@XmlElement(name = "source")
	private String source;// 来源（来自于哪里）
	@XmlElement(name = "abstracts")
	private String abstracts; // 摘要（140字符以内）
	@XmlElement(name = "thumbnails")
	private String thumbnails; // 缩略图（url相对地址）
	@XmlElement(name = "thumbnailsInfo")
	private String thumbnailsInfo; // 缩略图信息（即Thumbnails为空时）才需要
	@XmlElement(name = "tag")
	private String tag; // Tag信息
	@XmlElement(name = "cdurl")
	private String cdurl; // 财道url
	@XmlElement(name = "cdId")
	private String cdId; // 财道文章
	@XmlElement(name = "power")
	private String content; // 文章内容
	@XmlElement(name = "resultType")
	private int resultType;
	@XmlElement(name = "type")
	private String type;
	@XmlElement(name = "stock")
	private String stock; // 股票及市场（值为"股票code,市场code"）
	@XmlElement(name = "isAvailable")
	private int isAvailable;// 有效标识（1：标识使用中，0：逻辑删除）
	@XmlElement(name = "isNotice")
	private int isNotice;// 提醒标识（1：未提示，0：已提醒）
	@XmlElement(name = "userName")
	private String userName; // 用户昵称（唯一）

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

	/*
	 * (non-Javadoc)
	 * 
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
