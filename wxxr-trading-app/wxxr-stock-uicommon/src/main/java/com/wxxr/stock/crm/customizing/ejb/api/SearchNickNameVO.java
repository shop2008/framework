package com.wxxr.stock.crm.customizing.ejb.api;

import java.io.Serializable;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "SearchNickNameVO")
public class SearchNickNameVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private List<UserVO> nickList;

	/**
	 * @return the nickList
	 */
	
	public List<UserVO> getNickList() {
		return nickList;
	}

	/**
	 * @param nickList the nickList to set
	 */
	public void setNickList(List<UserVO> nickList) {
		this.nickList = nickList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SearchNickNameVO [nickList=" + nickList + "]";
	}
	
}
