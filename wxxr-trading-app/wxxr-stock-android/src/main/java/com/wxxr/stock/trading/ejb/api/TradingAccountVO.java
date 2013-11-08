package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TradingAccount")
public class TradingAccountVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 交易盘编号
	 */
	@XmlElement(name = "id")
	private long id;
	/**
	 * 买入日期  
	 */
	@XmlElement(name = "buyDay")
	private long buyDay;   
	/**
	 * 卖出日期 
	 */
	@XmlElement(name = "sellDay")	
	private long sellDay;  
	/**
	 * 申购金额
	 */
	@XmlElement(name = "applyFee")
	private long applyFee;
	/**
	 * 可用资金
	 */
	@XmlElement(name = "avalibleFee")
	private long avalibleFee;
	/**
	 * 总盈亏率
	 */
	@XmlElement(name = "gainRate")
	private long gainRate;  
	/**
	 * 总盈亏额
	 */
	@XmlElement(name = "totalGain")	
	private long totalGain;
	/**
	 * 交易综合费
	 */
	@XmlElement(name = "usedFee")
	private long usedFee;
	/**
	 * 止损
	 */
	@XmlElement(name = "lossLimit")
	private float lossLimit;//ֹ
	/**
	 * 冻结资金
	 */
	@XmlElement(name = "frozenVol")
	private long frozenVol;
	/**
	 * 最大持股代码
	 */
	@XmlElement(name = "maxStockCode")
	private String maxStockCode;
	/**
	 * 最大持股名称
	 */
	@XmlElement(name = "maxStockMarket")
	private String maxStockMarket;
	/**
	 * 交易盘类型
	 */
	@XmlElement(name = "type")
	private String type;
	/**
	 * 交易订单
	 */
	@XmlElement(name = "tradingOrders")
	private List<StockTradingOrderVO> tradingOrders;//
	/**
	 * 交易盘状态 CLOSED-已结算；UNCLOSE-未结算,CLEARING-正在结算
	 */
	@XmlElement(name = "over")
	private String over;
	/**是否为模拟盘*/
	@XmlElement(name = "virtual")
	private boolean virtual;
	/**
	 * 1:表示T日交易盘,0:T+1日交易盘
	 */
	@XmlElement(name = "status")
	private int status;
	/**
	 * @return the id
	 */
	
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the buyDay
	 */
	public long getBuyDay() {
		return buyDay;
	}
	/**
	 * @param buyDay the buyDay to set
	 */
	public void setBuyDay(long buyDay) {
		this.buyDay = buyDay;
	}
	/**
	 * @return the sellDay
	 */
	public long getSellDay() {
		return sellDay;
	}
	/**
	 * @param sellDay the sellDay to set
	 */
	public void setSellDay(long sellDay) {
		this.sellDay = sellDay;
	}
	/**
	 * @return the applyFee
	 */
	public long getApplyFee() {
		return applyFee;
	}
	/**
	 * @param applyFee the applyFee to set
	 */
	public void setApplyFee(long applyFee) {
		this.applyFee = applyFee;
	}
	/**
	 * @return the avalibleFee
	 */
	
	public long getAvalibleFee() {
		return avalibleFee;
	}
	/**
	 * @param avalibleFee the avalibleFee to set
	 */
	public void setAvalibleFee(long avalibleFee) {
		this.avalibleFee = avalibleFee;
	}
	
	/**
	 * @return the usedFee
	 */
	
	public long getUsedFee() {
		return usedFee;
	}
	/**
	 * @param usedFee the usedFee to set
	 */
	public void setUsedFee(long usedFee) {
		this.usedFee = usedFee;
	}
	/**
	 * @return the lossLimit
	 */
	
	public float getLossLimit() {
		return lossLimit;
	}
	/**
	 * @param lossLimit the lossLimit to set
	 */
	public void setLossLimit(float lossLimit) {
		this.lossLimit = lossLimit;
	}
	/**
	 * @return the frozenVol
	 */
	
	public long getFrozenVol() {
		return frozenVol;
	}
	/**
	 * @param frozenVol the frozenVol to set
	 */
	public void setFrozenVol(long frozenVol) {
		this.frozenVol = frozenVol;
	}
	
	/**
	 * @return the proVal
	 */
	
	public long getTotalGain() {
		return totalGain;
	}
	/**
	 * @return the proVal
	 */
	public void setTotalGain(long totalGain) {
		this.totalGain = totalGain;
	}
	/**
	 * @return the proRate
	 */
	
	public long getGainRate() {
		return gainRate;
	}
	/**
	 * @param proRate the proRate to set
	 */
	public void setGainRate(long gainRate) {
		this.gainRate = gainRate;
	}
	/**
	 * @return the tradingOrders
	 */
	
	public List<StockTradingOrderVO> getTradingOrders() {
		return tradingOrders;
	}
	/**
	 * @param tradingOrders the tradingOrders to set
	 */
	public void setTradingOrders(List<StockTradingOrderVO> tradingOrders) {
		this.tradingOrders = tradingOrders;
	}
	/**
	 * @return the maxStockCode
	 */
	
	public String getMaxStockCode() {
		return maxStockCode;
	}
	/**
	 * @param maxStockCode the maxStockCode to set
	 */
	public void setMaxStockCode(String maxStockCode) {
		this.maxStockCode = maxStockCode;
	}
	/**
	 * @return the maxStockMarket
	 */
	
	public String getMaxStockMarket() {
		return maxStockMarket;
	}
	/**
	 * @param maxStockMarket the maxStockMarket to set
	 */
	public void setMaxStockMarket(String maxStockMarket) {
		this.maxStockMarket = maxStockMarket;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public String getOver() {
		return over;
	}
	/**
	 * 
	 * @param over
	 */
	public void setOver(String over) {
		this.over = over;
	}
	
	
	
	
	public boolean isVirtual() {
		return virtual;
	}
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the type
	 */
	
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TradingAccountVO [applyFee=" + applyFee + ", avalibleFee="
				+ avalibleFee + ", buyDay=" + buyDay + ", frozenVol="
				+ frozenVol + ", gainRate=" + gainRate + ", id=" + id
				+ ", lossLimit=" + lossLimit + ", maxStockCode=" + maxStockCode
				+ ", maxStockMarket=" + maxStockMarket + ", over=" + over
				+ ", sellDay=" + sellDay + ", status=" + status
				+ ", totalGain=" + totalGain + ", tradingOrders="
				+ tradingOrders + ", usedFee=" + usedFee + ", virtual="
				+ virtual +", type="+type+ "]";
	}
	


	
}
