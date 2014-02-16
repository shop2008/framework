package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "StrategyConfigVO")
public class LossRateNDepositRate implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String lossRate;//止损率
	private String depositCash;//保证金比例
	/**
	 * @return the lossRate
	 */
	public String getLossRate() {
		return lossRate;
	}
	/**
	 * @param lossRate the lossRate to set
	 */
	public void setLossRate(String lossRate) {
		this.lossRate = lossRate;
	}
	/**
	 * @return the depositCash
	 */
	public String getDepositCash() {
		return depositCash;
	}
	/**
	 * @param depositCash the depositCash to set
	 */
	public void setDepositCash(String depositCash) {
		this.depositCash = depositCash;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LossRateNDepositRate [lossRate=" + lossRate + ", depositCash=" + depositCash + "]";
	}
	
}
