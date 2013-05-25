package com.wxxr.mobile.core.rpc.impl;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "stockarticlequery")
public class StockArticleQuery {
	
	
	private String stockId;
	
	private String marketCode;
	
	private String type;
	private int start;
	private int limit;

	 
	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	 
	public String getMarketCode() {
		return marketCode;
	}

	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}

	 
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	

}
