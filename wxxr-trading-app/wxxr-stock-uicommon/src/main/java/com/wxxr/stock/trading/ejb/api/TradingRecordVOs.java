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
@XmlRootElement(name = "TradingRecordVOs")
public class TradingRecordVOs {

	@XmlElement(name="tradingRecords")
	private List<TradingRecordVO> tradingRecords;

	/**
	 * @return the tradingRecords
	 */
	
	public List<TradingRecordVO> getTradingRecords() {
		return tradingRecords;
	}

	/**
	 * @param tradingRecords the tradingRecords to set
	 */
	public void setTradingRecords(List<TradingRecordVO> tradingRecords) {
		this.tradingRecords = tradingRecords;
	}
	
	
}
