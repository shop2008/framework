package com.wxxr.stock.crm.customizing.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserAttributeVO")
public class UserAttributeVO implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "userId")
	private String userId;// 用户ID
	@XmlElement(name = "attrName")
	private String attrName;//属性名
	@XmlElement(name = "attrValue")
	private String attrValue;//属性值
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
	 * @return the attrName
	 */
	public String getAttrName() {
		return attrName;
	}
	/**
	 * @param attrName the attrName to set
	 */
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	/**
	 * @return the attrValue
	 */
	public String getAttrValue() {
		return attrValue;
	}
	/**
	 * @param attrValue the attrValue to set
	 */
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserAttributeVO [userId=" + userId + ", attrName=" + attrName + ", attrValue=" + attrValue + "]";
	}
	

}
