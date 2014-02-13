package com.wxxr.stock.trading.ejb.api;


import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "TradingAccInfoVO")
public class TradingAccInfoVO implements Serializable{
	
	private static final long serialVersionUID = -6493252477259626791L;
	@XmlElement(name = "acctID")
	private Long acctID;//id
	@XmlElement(name = "maxStockName")
	private String maxStockName;//最大持股名称
	@XmlElement(name = "maxStockCode")
	private String maxStockCode;//最大持股代码
	@XmlElement(name = "maxStockMarket")
	private String maxStockMarket;//最大持股市场
	@XmlElement(name = "virtual")
	private boolean virtual;//交易盘类型
	@XmlElement(name = "sum")
	private Long sum;//申请资金
	@XmlElement(name = "totalGain")
	private Long totalGain;//总盈亏
	@XmlElement(name = "status")
	private int status;//1:表示可买,0:表示可卖
	@XmlElement(name = "over")
	private String over; //CLOSED表示已经完结,"UNCLOSE"表示未完结
	@XmlElement(name = "createDate")
	private long createDate;// 交易盘创建时间
	
	@XmlElement(name = "acctType")
	private String acctType;// 交易盘类型:ASTCOKT1,	ASTOCKT3,ASTCOKTN;

	
	public String getMaxStockName() {
		return maxStockName;
	}
	public void setMaxStockName(String maxStockName) {
		this.maxStockName = maxStockName;
	}
	
	public String getMaxStockCode() {
		return maxStockCode;
	}
	public void setMaxStockCode(String maxStockCode) {
		this.maxStockCode = maxStockCode;
	}
	
	public boolean isVirtual() {
		return virtual;
	}
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	
	public Long getSum() {
		return sum;
	}
	public void setSum(Long sum) {
		this.sum = sum;
	}
	
	public Long getTotalGain() {
		return totalGain;
	}
	public void setTotalGain(Long totalGain) {
		this.totalGain = totalGain;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	 * @return the over
	 */
	
	public String getOver() {
		return over;
	}
	/**
	 * @param over the over to set
	 */
	public void setOver(String over) {
		this.over = over;
	}
	
	/**
	 * @return the createDate
	 */
	
	public long getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
	/**
	 * @return the acctID
	 */
	
	public Long getAcctID() {
		return acctID;
	}
	/**
	 * @param acctID the acctID to set
	 */
	public void setAcctID(Long acctID) {
		this.acctID = acctID;
	}
	public String getAcctType() {
		return acctType;
	}
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}
	@Override
	public String toString() {
		return "TradingAccInfoVO [acctID=" + acctID + ", maxStockName="
				+ maxStockName + ", maxStockCode=" + maxStockCode
				+ ", maxStockMarket=" + maxStockMarket + ", virtual=" + virtual
				+ ", sum=" + sum + ", totalGain=" + totalGain + ", status="
				+ status + ", over=" + over + ", createDate=" + createDate
				+ ", acctType=" + acctType + "]";
	}
	
	
}
