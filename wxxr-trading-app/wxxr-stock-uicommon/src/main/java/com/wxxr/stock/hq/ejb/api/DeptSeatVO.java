package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DeptSeat")
public class DeptSeatVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "deptseatname")
	private String deptseatname;
	@XmlElement(name = "buy")
	private Double buy;
	@XmlElement(name = "starrate")
	private Double starrate;
	
	
	public String getDeptseatname() {
		return deptseatname;
	}
	public void setDeptseatname(String deptseatname) {
		this.deptseatname = deptseatname;
	}
	
	public Double getBuy() {
		return buy;
	}
	public void setBuy(Double buy) {
		this.buy = buy;
	}
	
	public Double getStarrate() {
		return starrate;
	}
	public void setStarrate(Double starrate) {
		this.starrate = starrate;
	}
	
	
}
