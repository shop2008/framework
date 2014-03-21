package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.notification.ejb.api.MessageVO;


@XmlRootElement(name = "SecurityAppHomePageVO")
public class SecurityAppHomePageVO implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private UserSignVO signMessage;
	private List<MessageVO> remindMessage;
	private List<TradingAccInfoVO> tradingAcctList;
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
	 * @return the remindMessage
	 */
	public List<MessageVO> getRemindMessage() {
		return remindMessage;
	}
	/**
	 * @param remindMessage the remindMessage to set
	 */
	public void setRemindMessage(List<MessageVO> remindMessage) {
		this.remindMessage = remindMessage;
	}
	/**
	 * @return the tradingAcctList
	 */
	public List<TradingAccInfoVO> getTradingAcctList() {
		return tradingAcctList;
	}
	/**
	 * @param tradingAcctList the tradingAcctList to set
	 */
	public void setTradingAcctList(List<TradingAccInfoVO> tradingAcctList) {
		this.tradingAcctList = tradingAcctList;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SecurityAppHomePageVO [signMessage=" + signMessage + ", remindMessage=" + remindMessage + ", tradingAcctList=" + tradingAcctList + "]";
	}
	
	
}
