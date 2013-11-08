package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StongStatisticsTw")
public class StongStatisticsTwVO implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private String maxChange;
	private String lastUpdate;
	private List<StockVO> stock;
	
	@XmlElement
	public String getMaxChange() {
		return maxChange;
	}
	public void setMaxChange(String maxChange) {
		this.maxChange = maxChange;
	}
	
	@XmlElement
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	@XmlElement
	public List<StockVO> getStock() {
		return stock;
	}
	public void setStock(List<StockVO> stock) {
		this.stock = stock;
	}
	
	
}
