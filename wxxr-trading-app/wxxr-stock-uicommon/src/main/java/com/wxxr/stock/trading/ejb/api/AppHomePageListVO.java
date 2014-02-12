package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wxxr.stock.notification.ejb.api.MessageVO;

@XmlRootElement(name = "AppHomePageListVO")
public class AppHomePageListVO implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private MegagameRankNUpdateTimeVO rankVo;
	private UserSignVO signMessage;
	private List<MessageVO> remindMessage;
	private List<PullMessageVO> pullMessageList;
	
	/**
	 * @return the list
	 */
	@XmlElement
	public MegagameRankNUpdateTimeVO getRankVo() {
		return rankVo;
	}
	/**
	 * @param list the list to set
	 */
	public void setRankList(MegagameRankNUpdateTimeVO rankVo) {
		this.rankVo = rankVo;
	}
	/**
	 * @return the signMessage
	 */
	@XmlElement
	public UserSignVO getSignMessage() {
		return signMessage;
	}
	/**
	 * @param signMessage the signMessage to set
	 */
	public void setSignMessage(UserSignVO signMessage) {
		this.signMessage = signMessage;
	}
	/**
	 * @return the findById
	 */
	@XmlElement
	public List<MessageVO> getRemindMessage() {
		return remindMessage;
	}
	/**
	 * @param findById the findById to set
	 */
	public void setRemindMessage(List<MessageVO> remindMessage) {
		this.remindMessage = remindMessage;
	}
	/**
	 * @return the pullMessageList
	 */
	@XmlElement
	public List<PullMessageVO> getPullMessageList() {
		return pullMessageList;
	}
	/**
	 * @param pullMessageList the pullMessageList to set
	 */
	public void setPullMessageList(List<PullMessageVO> pullMessageList) {
		this.pullMessageList = pullMessageList;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppHomePageListVO [rankVo=" + rankVo + ", signMessage=" + signMessage + ", remindMessage=" + remindMessage + ", pullMessageList=" + pullMessageList + "]";
	}
	
}
