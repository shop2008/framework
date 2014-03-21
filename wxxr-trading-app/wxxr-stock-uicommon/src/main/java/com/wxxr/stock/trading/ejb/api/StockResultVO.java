package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "StockResult")
public class StockResultVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "cause")
	private String cause;//失败的原因
	@XmlElement(name = "succOrNot")
	private int succOrNot;//0:表示失败 1：表示成功 
	/**
	 * @return the cause
	 */
	
	public String getCause() {
		return cause;
	}
	/**
	 * @param cause the cause to set
	 */
	public void setCause(String cause) {
		this.cause = cause;
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
		return "StockResultShow [cause=" + cause + ", succOrNot=" + succOrNot + "]";
	}

}
