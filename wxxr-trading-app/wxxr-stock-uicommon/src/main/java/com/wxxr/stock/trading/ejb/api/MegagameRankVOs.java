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
@XmlRootElement(name = "MegagameRankVOs")
public class MegagameRankVOs {

	@XmlElement(name="megagameRanks")
	private List<MegagameRankVO> megagameRanks;

	/**
	 * @return the megagameRanks
	 */
	
	public List<MegagameRankVO> getMegagameRanks() {
		return megagameRanks;
	}

	/**
	 * @param megagameRanks the megagameRanks to set
	 */
	public void setMegagameRanks(List<MegagameRankVO> megagameRanks) {
		this.megagameRanks = megagameRanks;
	}
	
	
}
