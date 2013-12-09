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
@XmlRootElement(name = "TradingAccInfoVOs")
public class TradingAccInfoVOs {

	@XmlElement(name="tradingAccInfos")
	private List<TradingAccInfoVO> tradingAccInfos;

	/**
	 * @return the tradingAccInfos
	 */
	
	public List<TradingAccInfoVO> getTradingAccInfos() {
		return tradingAccInfos;
	}

	/**
	 * @param tradingAccInfos the tradingAccInfos to set
	 */
	public void setTradingAccInfos(List<TradingAccInfoVO> tradingAccInfos) {
		this.tradingAccInfos = tradingAccInfos;
	}

	
	
	
}
