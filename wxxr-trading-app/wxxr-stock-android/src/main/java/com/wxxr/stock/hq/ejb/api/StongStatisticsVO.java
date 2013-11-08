package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StongStatistics")
public class StongStatisticsVO implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private String morrowHighs;
	private String morrowAvgHighs;
	private String morrowChange;
	private String morrowAvgChange;
	private String maxChange;
	private String lastUpdate;
	private List<StockVO> strongHighsHighs;
	private List<StockVO> strongHighsChange;
	
	@XmlElement
	public String getMorrowHighs() {
		return morrowHighs;
	}

	public void setMorrowHighs(String morrowHighs) {
		this.morrowHighs = morrowHighs;
	}
	@XmlElement
	public String getMorrowAvgHighs() {
		return morrowAvgHighs;
	}

	public void setMorrowAvgHighs(String morrowAvgHighs) {
		this.morrowAvgHighs = morrowAvgHighs;
	}
	@XmlElement
	public String getMorrowChange() {
		return morrowChange;
	}

	public void setMorrowChange(String morrowChange) {
		this.morrowChange = morrowChange;
	}
	@XmlElement
	public String getMorrowAvgChange() {
		return morrowAvgChange;
	}

	public void setMorrowAvgChange(String morrowAvgChange) {
		this.morrowAvgChange = morrowAvgChange;
	}
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
	public List<StockVO> getStrongHighsHighs() {
		return strongHighsHighs;
	}

	public void setStrongHighsHighs(List<StockVO> strongHighsHighs) {
		this.strongHighsHighs = strongHighsHighs;
	}
	@XmlElement
	public List<StockVO> getStrongHighsChange() {
		return strongHighsChange;
	}

	public void setStrongHighsChange(List<StockVO> strongHighsChange) {
		this.strongHighsChange = strongHighsChange;
	}

	
	
}
