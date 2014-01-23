/**
 * 
 */
package com.wxxr.mobile.stock.app.v2.bean;

/**
 * @author wangxuyang
 *
 */
public class ChampionShipMessageMenuItem extends BaseMenuItem {
	private String nickName;
	private String stockName;
	private Long incomeRate;//收益率
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public Long getIncomeRate() {
		return incomeRate;
	}
	public void setIncomeRate(Long incomeRate) {
		this.incomeRate = incomeRate;
	}
	@Override
	public String toString() {
		return "ChampionShipMessageMenuItem [nickName=" + nickName
				+ ", stockName=" + stockName + ", incomeRate=" + incomeRate
				+ "]";
	}
	
}
