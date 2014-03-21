package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author chenchao
 *
 */
@XmlRootElement(name = "GainPayDetails")
public class GainPayDetailsVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id; //id
	@XmlElement(name = "comment")
	private String comment;
	@XmlElement(name = "amount")
	private long amount;
	@XmlElement(name = "time")
	private long time;
	
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	

	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "GainPayDetailsVO [comment=" + comment + ", amount=" + amount
				+ ", time=" + time + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
