package com.wxxr.stock.restful.json;

import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "stockParams")
public class StockParamsVo {
	
	@XmlElement(name="stocks")
	private List<StockParamVo> stocks;

	/**
	 * @return the stocks
	 */
	public List<StockParamVo> getStocks() {
		return stocks;
	}

	/**
	 * @param stocks the stocks to set
	 */
	public void setStocks(List<StockParamVo> stocks) {
		this.stocks = stocks;
	}

	
	
	
	
}
