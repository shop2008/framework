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
@XmlRootElement(name = "HomePageVOs")
public class HomePageVOs {

	@XmlElement(name="homePages")
	private List<HomePageVO> homePages;

	/**
	 * @return the homePages
	 */

	public List<HomePageVO> getHomePages() {
		return homePages;
	}

	/**
	 * @param homePages the homePages to set
	 */
	public void setHomePages(List<HomePageVO> homePages) {
		this.homePages = homePages;
	}
	
	
}
