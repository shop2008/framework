package com.wxxr.mobile.stock.client.bean;

import java.util.List;

public class UserInfoEntity {

	/**
	 * 用户形象照 
	 */
	private String uIcon;
	
	/**
	 * 用户昵称
	 */
	private String uNickName;
	
	/**
	 * 用户累计实盘积分
	 */
	private String uIntegrals;
	
	/**
	 * 用户总收益
	 */
	private String uProfits;
	
	/**
	 * 挑战交易盘分享笔数
	 */
	private String uChallengeShared;
	
	/**
	 * 参赛交易盘分享笔数
	 */
	private String uJoinShared;
	
	
	/**
	 * 历史交易记录信息
	 */
	private List<TradeRecordEntity> records;


	public String getuIcon() {
		return uIcon;
	}


	public void setuIcon(String uIcon) {
		this.uIcon = uIcon;
	}


	public String getuNickName() {
		return uNickName;
	}


	public void setuNickName(String uNickName) {
		this.uNickName = uNickName;
	}


	public String getuIntegrals() {
		return uIntegrals;
	}


	public void setuIntegrals(String uIntegrals) {
		this.uIntegrals = uIntegrals;
	}


	public String getuProfits() {
		return uProfits;
	}


	public void setuProfits(String uProfits) {
		this.uProfits = uProfits;
	}


	public String getuChallengeShared() {
		return uChallengeShared;
	}


	public void setuChallengeShared(String uChallengeShared) {
		this.uChallengeShared = uChallengeShared;
	}


	public String getuJoinShared() {
		return uJoinShared;
	}


	public void setuJoinShared(String uJoinShared) {
		this.uJoinShared = uJoinShared;
	}


	public List<TradeRecordEntity> getRecords() {
		return records;
	}


	public void setRecords(List<TradeRecordEntity> records) {
		this.records = records;
	}
}
