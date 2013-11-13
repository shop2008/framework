package com.wxxr.mobile.stock.client.bean;

import java.util.List;

public class ScoreInfoEntity {

	/**
	 * 积分余额
	 */
	private String balance;
	
	/**
	 * 所有积分信息
	 */
	private List<Score> scores;

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public List<Score> getScores() {
		return scores;
	}

	public void setScores(List<Score> scores) {
		this.scores = scores;
	}
}
