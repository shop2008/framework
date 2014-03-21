/**
 * 
 */
package com.wxxr.stock.restful.json;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.trading.ejb.api.PullMessageVO;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name = "PullMessageVOs")
public class PullMessageVOs {
	
	@XmlElement(name="pullMessages")
	private List<PullMessageVO> pullMessages;

	/**
	 * @return the pullMessages
	 */
	
	public List<PullMessageVO> getPullMessages() {
		return pullMessages;
	}

	/**
	 * @param pullMessages the pullMessages to set
	 */
	public void setPullMessages(List<PullMessageVO> pullMessages) {
		this.pullMessages = pullMessages;
	}
	
	
}
