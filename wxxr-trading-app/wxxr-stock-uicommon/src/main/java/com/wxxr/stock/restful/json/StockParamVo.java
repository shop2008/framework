package com.wxxr.stock.restful.json;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "stockParam")
public class StockParamVo {
	@XmlElement(name="code")
    private String code;
	@XmlElement(name="market")
    private String market;
	/**
	 * @return the code
	 */
    
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the market
	 */
	public String getMarket() {
		return market;
	}
	/**
	 * @param market the market to set
	 */
	public void setMarket(String market) {
		this.market = market;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StockParamVo [code=" + code + ", market=" + market + "]";
	}
    
    
}
