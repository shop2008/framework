/**
 * 
 */
package com.wxxr.mobile.stock.client.bean;

import java.util.List;

/**
 * @author wangxuyang
 * 
 */
public class User {
	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 用户形象照
	 */
	private String userPic;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 电话号吗
	 */
	private String phoneNumber;

	/**
	 * 积分
	 */
	private String score;

	/**
	 * 余额
	 */
	private String balance;

	/**
	 * 累计总收益
	 */
	private String totoalProfit;

	/**
	 * 累计实盘积分
	 */
	private String totoalScore;

	/**
	 * 挑战交易盘分享笔数
	 */
	private String challengeShared;

	/**
	 * 参赛交易盘分享笔数
	 */
	private String joinShared;

	private List<TradingAccount> tradeInfos;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getTotoalProfit() {
		return totoalProfit;
	}

	public void setTotoalProfit(String totoalProfit) {
		this.totoalProfit = totoalProfit;
	}

	public String getTotoalScore() {
		return totoalScore;
	}

	public void setTotoalScore(String totoalScore) {
		this.totoalScore = totoalScore;
	}

	public String getChallengeShared() {
		return challengeShared;
	}

	public void setChallengeShared(String challengeShared) {
		this.challengeShared = challengeShared;
	}

	public String getJoinShared() {
		return joinShared;
	}

	public void setJoinShared(String joinShared) {
		this.joinShared = joinShared;
	}

	public List<TradingAccount> getTradeInfos() {
		return tradeInfos;
	}

	public void setTradeInfos(List<TradingAccount> tradeInfos) {
		this.tradeInfos = tradeInfos;
	}
}
