package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "WeekRankVO")
public class WeekRankVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "uesrId")
	private String uesrId;//id
	@XmlElement(name = "nickName")
	private String nickName;//昵称
	@XmlElement(name = "gainCount")
	private int gainCount;//正收益个数1
	@XmlElement(name = "totalGain")
	private Long totalGain;//总盈亏3
	@XmlElement(name = "gainRate")
	private String gainRate;//总盈亏率2
	@XmlElement(name = "gainRates")
	private int gainRates;
	@XmlElement(name = "dates")
	private String dates;//周期时间

	
	
	
	public String getUesrId() {
		return uesrId;
	}
	public void setUesrId(String uesrId) {
		this.uesrId = uesrId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public int getGainCount() {
		return gainCount;
	}
	public void setGainCount(int gainCount) {
		this.gainCount = gainCount;
	}
	
	public Long getTotalGain() {
		return totalGain;
	}
	public void setTotalGain(Long totalGain) {
		this.totalGain = totalGain;
	}
	
	public String getGainRate() {
		return gainRate;
	}
	public void setGainRate(String gainRate) {
		this.gainRate = gainRate;
	}
	
	public String getDates() {
		return dates;
	}
	public void setDates(String dates) {
		this.dates = dates;
	}
	
	
	public int getGainRates() {
		return gainRates;
	}
	public void setGainRates(int gainRates) {
		this.gainRates = gainRates;
	}
	@Override
	public String toString() {
		return "WeekRankVO [nickName=" + nickName + ", gainCount=" + gainCount
				+ ", totalGain=" + totalGain + ", gainRate=" + gainRate
				+ ", gainRates=" + gainRates + ", dates=" + dates + "]";
	}
	
	
}
