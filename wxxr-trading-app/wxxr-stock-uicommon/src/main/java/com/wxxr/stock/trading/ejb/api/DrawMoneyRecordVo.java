package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "DrawMoneyRecordVo")
public class DrawMoneyRecordVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "drawDate")
	private String drawDate;
	@XmlElement(name = "drawAmount")
	private Long drawAmount;
	@XmlElement(name = "drawState")
	private String drawState;
	@XmlElement(name = "id")
	private Long id;
	/**
	 * @return the drawDate
	 */
	
	public String getDrawDate() {
		return drawDate;
	}
	/**
	 * @param drawDate the drawDate to set
	 */
	public void setDrawDate(String drawDate) {
		this.drawDate = drawDate;
	}
	/**
	 * @return the drawAmount
	 */

	public Long getDrawAmount() {
		return drawAmount;
	}
	/**
	 * @param drawAmount the drawAmount to set
	 */
	public void setDrawAmount(Long drawAmount) {
		this.drawAmount = drawAmount;
	}
	/**
	 * @return the drawState
	 */
	
	public String getDrawState() {
		return drawState;
	}
	/**
	 * @param drawState the drawState to set
	 */
	public void setDrawState(String drawState) {
		this.drawState = drawState;
	}
	
	/**
	 * @return the id
	 */
	
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DrawMoneyRecordVo [drawDate=" + drawDate + ", drawAmount=" + drawAmount + ", drawState=" + drawState + ", id=" + id + "]";
	}
	
}
