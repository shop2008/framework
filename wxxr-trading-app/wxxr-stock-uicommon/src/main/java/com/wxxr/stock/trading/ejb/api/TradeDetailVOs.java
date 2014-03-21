/**
 * 
 */
package com.wxxr.stock.trading.ejb.api;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
import com.wxxr.stock.hq.ejb.api.TradeDetailVO;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name="tradeDetailVOs")
public class TradeDetailVOs {

	@XmlElement(name="tradeDetails")
	private List<TradeDetailVO> tradeDetails;

	/**
	 * @return the tradeDetails
	 */
	public List<TradeDetailVO> getTradeDetails() {
		return tradeDetails;
	}

	/**
	 * @param tradeDetails the tradeDetails to set
	 */
	public void setTradeDetails(List<TradeDetailVO> tradeDetails) {
		this.tradeDetails = tradeDetails;
	}

	
	
	
	
}
