package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.notification.ejb.api.MessageVO;
@XmlRootElement(name = "AppHomePageListVO")
public class AppHomePageListVO implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	@XmlElement(name="rankVo")
	private MegagameRankNUpdateTimeVO rankVo;//rankVo
	@XmlElement(name="signMessage")
	private UserSignVO signMessage;
	@XmlElement(name="remindMessage")
	private List<MessageVO> remindMessage;
	@XmlElement(name="pullMessageList")
	private List<PullMessageVO> pullMessageList;
	
	/**
	 * @return the list
	 */
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
