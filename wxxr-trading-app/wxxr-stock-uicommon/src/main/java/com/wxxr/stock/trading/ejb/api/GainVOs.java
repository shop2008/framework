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
@XmlRootElement(name = "GainVOs")
public class GainVOs {

	@XmlElement(name="gains")
	private List<GainVO> gains;

	/**
	 * @return the gains
	 */
	
	public List<GainVO> getGains() {
		return gains;
	}

	/**
	 * @param gains the gains to set
	 */
	public void setGains(List<GainVO> gains) {
		this.gains = gains;
	}
	
	
}
