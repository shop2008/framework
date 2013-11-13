package com.wxxr.mobile.stock.client.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;

@BindableBean(pkg="com.wxxr.mobile.stock.client.bean",className="AccountInfoBean")
public class AccountInfo {

	/**
	 * 余额
	 */
	private String balance;
	
	/**
	 * 冻结
	 */
	private String freeze;
	
	/**
	 * 可用
	 */
	private String avalible;
	
	/**
	 * 积分余额
	 */
	private String scoreBalance;
	
	
	/**
	 * 积分信息
	 */
	private List<Score> scores;
	
	/**
	 * 收支明细
	 */
	private List<TradeDetail> tradeDetails;

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}


	public String getFreeze() {
		return freeze;
	}

	public void setFreeze(String freeze) {
		this.freeze = freeze;
	}

	public String getAvalible() {
		return avalible;
	}

	public void setAvalible(String avalible) {
		this.avalible = avalible;
	}

	public String getScoreBalance() {
		return scoreBalance;
	}

	public void setScoreBalance(String scoreBalance) {
		this.scoreBalance = scoreBalance;
	}

	public List<Score> getScores() {
		return scores;
	}

	public void setScores(List<Score> scores) {
		this.scores = scores;
	}

	public List<TradeDetail> getTradeDetails() {
		return tradeDetails;
	}

	public void setTradeDetails(List<TradeDetail> tradeDetails) {
		this.tradeDetails = tradeDetails;
	}
	
}
