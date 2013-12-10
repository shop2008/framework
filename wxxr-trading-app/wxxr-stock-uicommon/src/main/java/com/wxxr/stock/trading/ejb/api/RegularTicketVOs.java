/**
 * 
 */
package com.wxxr.stock.trading.ejb.api;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name = "RegularTicketVOs")
public class RegularTicketVOs {
	@XmlElement(name="regularTickets")
	private List<RegularTicketVO> regularTickets;

	/**
	 * @return the regularTickets
	 */
	
	public List<RegularTicketVO> getRegularTickets() {
		return regularTickets;
	}

	/**
	 * @param regularTickets the regularTickets to set
	 */
	public void setRegularTickets(List<RegularTicketVO> regularTickets) {
		this.regularTickets = regularTickets;
	}
	
	
}
