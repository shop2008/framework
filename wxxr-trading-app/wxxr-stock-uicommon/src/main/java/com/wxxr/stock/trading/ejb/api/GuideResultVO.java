package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "GuideResult")
/**
 * 是否新手指引
 */
public class GuideResultVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long amount;//新手指引奖励数量
	private int succOrNot;//0:已经完成新手止盈 1：未新手指引 
	/**
	 * @return the cause
	 */
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	/**
	 * @return the succOrNot
	 */
	public int getSuccOrNot() {
		return succOrNot;
	}
	
	/**
	 * @param succOrNot the succOrNot to set
	 */
	public void setSuccOrNot(int succOrNot) {
		this.succOrNot = succOrNot;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GuideResultVO [amount=" + amount + ", succOrNot=" + succOrNot
				+ "]";
	}

}
