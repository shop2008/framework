package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author chenchao
 *
 */

@XmlRootElement(name = "UserAsset")
public class UserAssetVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "bal")
	private long bal;//余额
	@XmlElement(name = "usableBal")
	private long usableBal;//可用余额
	@XmlElement(name = "frozen")
	private long frozen;//冻结金额
	
	
	public long getBal() {
		return bal;
	}
	public void setBal(long bal) {
		this.bal = bal;
	}
	
	public long getUsableBal() {
		return usableBal;
	}
	public void setUsableBal(long usableBal) {
		this.usableBal = usableBal;
	}
	
	public long getFrozen() {
		return frozen;
	}
	public void setFrozen(long frozen) {
		this.frozen = frozen;
	}
	@Override
	public String toString() {
		return "UserAssetVO [bal=" + bal + ", frozen=" + frozen
				+ ", usableBal=" + usableBal + "]";
	}
	
	
}
