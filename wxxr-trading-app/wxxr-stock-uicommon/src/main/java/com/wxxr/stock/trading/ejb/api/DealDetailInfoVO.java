
package com.wxxr.stock.trading.ejb.api;

import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement(name = "DealDetailInfoVO")
public class DealDetailInfoVO{
	@XmlElement(name = "id")
	private long id;
	
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

	@XStreamImplicit(itemFieldName = "tradingRecords")
	private List<TradeRecordVO> tradingRecords;

	

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
	public List<TradeRecordVO> getTradingRecords() {
		return tradingRecords;
	}



	/**
	 * @param tradingRecords the tradingRecords to set
	 */
	public void setTradingRecords(List<TradeRecordVO> tradingRecords) {
		this.tradingRecords = tradingRecords;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DealDetailDTO [id=").append(id).append(", fund=")
				.append(fund).append(", plRisk=").append(plRisk)
				.append(", totalGain=").append(totalGain).append(", userGain=")
				.append(userGain).append(", imgUrl=")
				.append(Arrays.toString(imgUrl)).append(", tradingRecords=")
				.append(tradingRecords).append("]");
		return builder.toString();
	}
}
