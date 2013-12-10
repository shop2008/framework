/**
 * 
 */
package com.wxxr.stock.restful.json;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.notification.ejb.api.MessageVO;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name="messageVos")
public class MessageVOs {
	
	@XmlElement(name="messages")
	private List<MessageVO> messages;

	/**
	 * @return the messages
	 */
	
	public List<MessageVO> getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<MessageVO> messages) {
		this.messages = messages;
	}
	
}
