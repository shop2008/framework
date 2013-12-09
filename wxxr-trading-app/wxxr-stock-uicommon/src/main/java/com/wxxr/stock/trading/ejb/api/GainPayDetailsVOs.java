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
@XmlRootElement(name = "GainPayDetailsVOs")
public class GainPayDetailsVOs {

	@XmlElement(name="gainPayDetailss")
	private List<GainPayDetailsVO> gainPayDetailss;

	/**
	 * @return the gainPayDetailss
	 */
	
	public List<GainPayDetailsVO> getGainPayDetailss() {
		return gainPayDetailss;
	}

	/**
	 * @param gainPayDetailss the gainPayDetailss to set
	 */
	public void setGainPayDetailss(List<GainPayDetailsVO> gainPayDetailss) {
		this.gainPayDetailss = gainPayDetailss;
	}
	
}
