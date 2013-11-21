/**
 * 
 */
package com.wxxr.mobile.core.rpc.impl;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name = "user")
public class UserVO  implements Serializable{
	/**用户名*/
	@XmlElement(name = "userName")
	private String userName;
	/**昵称*/
	@XmlElement(name = "nickName")
	private String nickName;
	/**手机*/
	@XmlElement(name = "moblie")
	private String moblie;
	/**头像*/
	@XmlElement(name = "icon")
	private String icon;
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}
	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	/**
	 * @return the moblie
	 */
	public String getMoblie() {
		return moblie;
	}
	/**
	 * @param moblie the moblie to set
	 */
	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", nickName=" + nickName
				+ ", moblie=" + moblie + ", icon=" + icon + "]";
	}
}
