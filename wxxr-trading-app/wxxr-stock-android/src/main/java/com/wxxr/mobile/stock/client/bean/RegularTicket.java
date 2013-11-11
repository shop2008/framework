package com.wxxr.mobile.stock.client.bean;


public class RegularTicket{
	
	/**
	 * 
	 */
	private String nickName;//昵称
	private long regular;//实盘券数1
	private int gainCount;//正收益个数
	private int rankSeq;
	
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
	public int getRankSeq() {
		return rankSeq;
	}
	public void setRankSeq(int rankSeq) {
		this.rankSeq = rankSeq;
	}
	@Override
	public String toString() {
		return "RegularTicketVO [nickName=" + nickName + ", regular=" + regular
				+ ", gainCount=" + gainCount + "]";
	}
	
	

}
