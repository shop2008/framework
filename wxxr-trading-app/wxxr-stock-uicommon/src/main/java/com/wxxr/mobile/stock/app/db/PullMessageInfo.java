package com.wxxr.mobile.stock.app.db;

import com.wxxr.mobile.dao.annotation.Column;
import com.wxxr.mobile.dao.annotation.Entity;
import com.wxxr.mobile.dao.annotation.Id;
import com.wxxr.mobile.dao.annotation.Table;


@Entity
@Table(name="P_MSG")
public class PullMessageInfo {
	@Id
	private Long id;
	@Column(nullable=false)
	private String createDate;
	@Column(nullable=false)
	private String title;
	@Column(nullable=false)
	private String message;
	@Column
	private String articleUrl;
	@Column
	private String phone;
	@Column
	private boolean read;
	@Column
	private String userId;
	
	private Long pullId;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getArticleUrl() {
		return articleUrl;
	}

	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean getRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the pullId
	 */
	public Long getPullId() {
		return pullId;
	}

	/**
	 * @param pullId the pullId to set
	 */
	public void setPullId(Long pullId) {
		this.pullId = pullId;
	}





	
}
