package com.wxxr.stock.trading.ejb.api;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MegagameRankNUpdateTimeVO")
public class MegagameRankNUpdateTimeVO {
	
	private Long updateTime;
	private List<MegagameRankVO> rankList;
	/**
	 * @return the updateTime
	 */
	@XmlElement
	public Long getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the rankList
	 */
	@XmlElement
	public List<MegagameRankVO> getRankList() {
		return rankList;
	}
	/**
	 * @param rankList the rankList to set
	 */
	public void setRankList(List<MegagameRankVO> rankList) {
		this.rankList = rankList;
	}
	
	
	
}
