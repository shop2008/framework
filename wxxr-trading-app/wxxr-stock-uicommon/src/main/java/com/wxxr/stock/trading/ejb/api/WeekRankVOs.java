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
@XmlRootElement(name = "WeekRankVOs")
public class WeekRankVOs {

	@XmlElement(name="weekRanks")
	private List<WeekRankVO> weekRanks;

	/**
	 * @return the weekRanks
	 */
	
	public List<WeekRankVO> getWeekRanks() {
		return weekRanks;
	}

	/**
	 * @param weekRanks the weekRanks to set
	 */
	public void setWeekRanks(List<WeekRankVO> weekRanks) {
		this.weekRanks = weekRanks;
	}
	
	
}
