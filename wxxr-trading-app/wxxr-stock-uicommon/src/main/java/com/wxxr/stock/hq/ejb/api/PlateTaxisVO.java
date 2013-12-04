package com.wxxr.stock.hq.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * 板块排行VO
 * @author juyao
 *
 */
@XmlRootElement(name="PlateTaxis")
public class PlateTaxisVO implements Serializable {
	@XmlElement(name="id")
	private Long id;//板块id
	@XmlElement(name="code")
	private String code;
	@XmlElement(name="name")
	private String name;// 板块名称
	@XmlElement(name="risefallrate")
	private Long risefallrate;// 涨跌幅
	@XmlElement(name="stockname")
	private String stockname;//领涨股
	@XmlElement(name="market")
	private String market; //领涨股 市场
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	} 
	
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getRisefallrate() {
		return risefallrate;
	}
	public void setRisefallrate(Long risefallrate) {
		this.risefallrate = risefallrate;
	}
	
	public String getStockname() {
		return stockname;
	}
	public void setStockname(String stockname) {
		this.stockname = stockname;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PlateTaxisVO [id=" + id + ", code=" + code + ", name=" + name
				+ ", risefallrate=" + risefallrate + ", stockname=" + stockname
				+ ", market=" + market + "]";
	}
	

}
