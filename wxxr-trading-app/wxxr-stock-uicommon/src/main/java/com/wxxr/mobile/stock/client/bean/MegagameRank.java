package com.wxxr.mobile.stock.client.bean;

/**
 * T、T+1日排行榜
 * @author wangxuyang
 *
 */
public class MegagameRank{
	/**
	 * 昵称
	 */
	private String nickName;//昵称
	/**
	 * 1:表示今天,0:表示前一天
	 */
	private int status;
	/**
	 * 结算状态-CLOSED表示已经完结,"UNCLOSE"表示未完结
	 */
	private String over; 
	private String maxStockCode;//最大持股代码
	private String maxStockMarket;//最大持股市场
	private Long totalGain;//总盈亏2
	private String gainRate;//总盈亏率1
	private int gainRates;
	private String userId;//用户id
	private long acctID;
	private int rankSeq;
	
	
	
	public int getRankSeq() {
		return rankSeq;
	}
	public void setRankSeq(int rankSeq) {
		this.rankSeq = rankSeq;
	}
	public long getAcctID() {
		return acctID;
	}
	public void setAcctID(long acctID) {
		this.acctID = acctID;
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
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getOver() {
		return over;
	}
	public void setOver(String over) {
		this.over = over;
	}
	
	public String getMaxStockCode() {
		return maxStockCode;
	}
	public void setMaxStockCode(String maxStockCode) {
		this.maxStockCode = maxStockCode;
	}
	
	public String getMaxStockMarket() {
		return maxStockMarket;
	}
	public void setMaxStockMarket(String maxStockMarket) {
		this.maxStockMarket = maxStockMarket;
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
	
	
	public int getGainRates() {
		return gainRates;
	}
	public void setGainRates(int gainRates) {
		this.gainRates = gainRates;
	}
	@Override
	public String toString() {
		return "MegagameRankVO [nickName=" + nickName + ", status=" + status
				+ ", over=" + over + ", maxStockCode=" + maxStockCode
				+ ", maxStockMarket=" + maxStockMarket + ", totalGain="
				+ totalGain + ", gainRate=" + gainRate + ", gainRates="
				+ gainRates + "]";
	}
	
}
