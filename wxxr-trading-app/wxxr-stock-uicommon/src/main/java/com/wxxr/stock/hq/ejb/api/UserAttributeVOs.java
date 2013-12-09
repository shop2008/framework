/**
 * 
 */
package com.wxxr.stock.hq.ejb.api;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.crm.customizing.ejb.api.UserAttributeVO;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name = "UserAttributeVOs")
public class UserAttributeVOs {

	@XmlElement(name="userAttributes")
	private List<UserAttributeVO> userAttributes;

	/**
	 * @return the userAttributes
	 */
	
	public List<UserAttributeVO> getUserAttributes() {
		return userAttributes;
	}

	/**
	 * @param userAttributes the userAttributes to set
	 */
	public void setUserAttributes(List<UserAttributeVO> userAttributes) {
		this.userAttributes = userAttributes;
	}
	
	
}
