/**
 * 
 */
package com.wxxr.stock.hq.ejb.api;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name = "StockMinuteKs")
public class StockMinuteKVOs {
	
	@XmlElement(name="stockMinuteKVOs")
	private List<StockMinuteKVO> stockMinuteKVOs;

	/**
	 * @return the stockMinuteKVOs
	 */
	
	public List<StockMinuteKVO> getStockMinuteKVOs() {
		return stockMinuteKVOs;
	}

	/**
	 * @param stockMinuteKVOs the stockMinuteKVOs to set
	 */
	public void setStockMinuteKVOs(List<StockMinuteKVO> stockMinuteKVOs) {
		this.stockMinuteKVOs = stockMinuteKVOs;
	}
	
	
}
