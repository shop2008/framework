package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RegularTicketVO")
public class RegularTicketVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "nickName")
	private String nickName;//昵称
	@XmlElement(name = "regular")
	private long regular;//实盘券数1
	@XmlElement(name = "gainCount")
	private int gainCount;//正收益个数
	
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public long getRegular() {
		return regular;
	}
	public void setRegular(long regular) {
		this.regular = regular;
	}
	
	public int getGainCount() {
		return gainCount;
	}
	public void setGainCount(int gainCount) {
		this.gainCount = gainCount;
	}
	@Override
	public String toString() {
		return "RegularTicketVO [nickName=" + nickName + ", regular=" + regular
				+ ", gainCount=" + gainCount + "]";
	}
	
	

}
