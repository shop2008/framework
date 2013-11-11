package com.wxxr.mobile.stock.client.bean;


public class WeekRank{


	private String nickName;//昵称
	private int gainCount;//正收益个数1
	private Long totalGain;//总盈亏3
	private String gainRate;//总盈亏率2
	private int gainRates;
	private String dates;//周期时间
	private String userId;//用户id
	
	private int rankSeq;//
	
	public int getRankSeq() {
		return rankSeq;
	}
	public void setRankSeq(int rankSeq) {
		this.rankSeq = rankSeq;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
