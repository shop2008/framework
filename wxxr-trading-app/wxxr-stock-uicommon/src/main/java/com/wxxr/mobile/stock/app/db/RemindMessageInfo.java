package com.wxxr.mobile.stock.app.db;

import com.wxxr.mobile.dao.annotation.Column;
import com.wxxr.mobile.dao.annotation.Entity;
import com.wxxr.mobile.dao.annotation.Id;
import com.wxxr.mobile.dao.annotation.Table;

@Entity
@Table(name="R_MSG")
public class RemindMessageInfo {
	@Id
	private Long id;// 提醒ID
	@Column(nullable=false)
	private String type;// 提醒类型
	@Column(nullable=false)
	private String title;//标题
	@Column
	private String content;// 提醒内容
	@Column
	private String createdDate;
	@Column
	private String acctId;
	@Column
	private String attrs;
	@Column(nullable=false)
	private boolean read;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String acctId) {
		this.acctId = acctId;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	public boolean getRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	@Override
	public String toString() {
		return "RemindMessageInfo [id=" + id + ", type=" + type + ", title="
				+ title + ", content=" + content + ", createdDate="
				+ createdDate + ", acctId=" + acctId + ", attrs=" + attrs
				+ ", read=" + read + "]";
	}

}
