package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author chenchao
 *
 */
@XmlRootElement(name = "HomePageVO")
public class HomePageVO implements Serializable{

	private static final long serialVersionUID = 3846996765680952029L;
	@XmlElement(name = "accID")
	private String accID;//id
	@XmlElement(name = "url")
	private String url;
	/**文字*/
	@XmlElement(name = "wordage")
	private String wordage;
	
	
	public String getAccID() {
		return accID;
	}
	public void setAccID(String accID) {
		this.accID = accID;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getWordage() {
		return wordage;
	}
	public void setWordage(String wordage) {
		this.wordage = wordage;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HomePageVO [accID=" + accID + ", url=" + url + ", wordage="
				+ wordage + "]";
	}
	
	

}
