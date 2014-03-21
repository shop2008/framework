/**
 * 
 */
package com.wxxr.security.vo;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangyan
 *设置昵称
 */
@XmlRootElement(name = "UserInfoQuery")
public class UserParamVO implements Serializable {


	/**昵称*/
	@XmlElement(name = "nickName")
	private String nickName;

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
	
	
}
