package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "AppHomePageListVO")
public class AppHomePageListVO implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	@XmlElement(name="rankVo")
	private MegagameRankNUpdateTimeVO rankVo;//rankVo
	@XmlElement(name="pullMessageList")
	private List<PullMessageVO> pullMessageList;
	
	/**
	 * @return the list
	 */
	public MegagameRankNUpdateTimeVO getRankVo() {
		return rankVo;
	}
	/**
	 * @param list the list to set
	 */
	public void setRankVo(MegagameRankNUpdateTimeVO rankVo) {
		this.rankVo = rankVo;
	}

	/**
	 * @return the pullMessageList
	 */
	public List<PullMessageVO> getPullMessageList() {
		return pullMessageList;
	}
	/**
	 * @param pullMessageList the pullMessageList to set
	 */
	public void setPullMessageList(List<PullMessageVO> pullMessageList) {
		this.pullMessageList = pullMessageList;
	}
	@Override
	public String toString() {
		return "AppHomePageListVO [rankVo=" + rankVo + ", pullMessageList="
				+ pullMessageList + "]";
	}
	
	
}
