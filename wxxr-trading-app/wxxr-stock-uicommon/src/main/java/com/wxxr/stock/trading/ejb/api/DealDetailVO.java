/**
 * 
 */
package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;

/**
 * @author wangyan
 *
 */
@XmlRootElement(name = "DealDetailVO")
public class DealDetailVO implements Serializable{
	
	private static final long serialVersionUID = -8890599201016565210L;
	private String id;//id
	/**申请资金*/
	@XmlElement(name = "fund")
	private String fund;
	/**总盈亏率*/
	@XmlElement(name = "plRisk")
	private String plRisk;
	/**盈亏总额（交易盘，除去费用）*/
	@XmlElement(name = "totalGain")
	private String totalGain;
	/**玩家实得收益--没有收益时不显示80%*/
	@XmlElement(name = "userGain")
	private String userGain;
	@XmlElement(name = "imgUrl")
	private String[] imgUrl;
	@XmlElement(name = "tradingRecords")
	private List<TradingRecordVO> tradingRecords;

	/**
	 * @return the fund
	 */
	
	public String getFund() {
		return fund;
	}

	/**
	 * @param fund the fund to set
	 */
	public void setFund(String fund) {
		this.fund = fund;
	}

	/**
	 * @return the plRisk
	 */
	
	public String getPlRisk() {
		return plRisk;
	}

	/**
	 * @param plRisk the plRisk to set
	 */
	public void setPlRisk(String plRisk) {
		this.plRisk = plRisk;
	}

	/**
	 * @return the totalGain
	 */
	
	public String getTotalGain() {
		return totalGain;
	}

	/**
	 * @param totalGain the totalGain to set
	 */
	public void setTotalGain(String totalGain) {
		this.totalGain = totalGain;
	}

	/**
	 * @return the userGain
	 */
	
	public String getUserGain() {
		return userGain;
	}

	/**
	 * @param userGain the userGain to set
	 */
	public void setUserGain(String userGain) {
		this.userGain = userGain;
	}

	/**
	 * @return the imgUrl
	 */
	
	public String[] getImgUrl() {
		return imgUrl;
	}

	/**
	 * @param imgUrl the imgUrl to set
	 */
	public void setImgUrl(String[] imgUrl) {
		this.imgUrl = imgUrl;
	}

	/**
	 * @return the tradingRecords
	 */
	
	public List<TradingRecordVO> getTradingRecords() {
		return tradingRecords;
	}

	/**
	 * @param tradingRecords the tradingRecords to set
	 */
	public void setTradingRecords(List<TradingRecordVO> tradingRecords) {
		this.tradingRecords = tradingRecords;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "DealDetailVO [fund=" + fund + ", imgUrl="
				+ Arrays.toString(imgUrl) + ", plRisk=" + plRisk
				+ ", totalGain=" + totalGain + ", tradingRecords="
				+ tradingRecords + ", userGain=" + userGain + "]";
	}
	
	
}
