
package com.wxxr.stock.trading.ejb.api;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement(name = "TradeRecordVO")
public class TradeRecordVO{
	@XmlElement(name = "id")
	private long id;
	
	@XmlElement(name = "date")
	private long date;//日期
	@XmlElement(name = "market")
	private String market;//股票市场
	
	@XmlElement(name = "code")
	private String code;//股票代码
	
	@XmlElement(name = "describe")
	private String describe;//成交方向：
	
	@XmlElement(name = "price")
	private long price;//成交价格
	
	@XmlElement(name = "vol")
	private long vol;//成交量
	
	@XmlElement(name = "amount")
	private long amount;//成交金额
	
	@XmlElement(name = "brokerage")
	private long brokerage;//佣金
	
	@XmlElement(name = "tax")
	private long tax;//印花税
	
	@XmlElement(name = "fee")
	private long fee;//过户费
	
	@XmlElement(name = "day")
	private int day;//1:表示t日,0:表示t+1日
	
	@XmlElement(name = "beDone")
	private boolean beDone;//订单是否完成，DONE为true；否则为false
	
	
	
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
	 * @return the date
	 */
	public long getDate() {
		return date;
	}



	/**
	 * @param date the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}



	/**
	 * @return the market
	 */
	public String getMarket() {
		return market;
	}



	/**
	 * @param market the market to set
	 */
	public void setMarket(String market) {
		this.market = market;
	}



	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}



	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}



	/**
	 * @return the describe
	 */
	public String getDescribe() {
		return describe;
	}



	/**
	 * @param describe the describe to set
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}



	/**
	 * @return the price
	 */
	public long getPrice() {
		return price;
	}



	/**
	 * @param price the price to set
	 */
	public void setPrice(long price) {
		this.price = price;
	}



	/**
	 * @return the vol
	 */
	public long getVol() {
		return vol;
	}



	/**
	 * @param vol the vol to set
	 */
	public void setVol(long vol) {
		this.vol = vol;
	}



	/**
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}



	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}



	/**
	 * @return the brokerage
	 */
	public long getBrokerage() {
		return brokerage;
	}



	/**
	 * @param brokerage the brokerage to set
	 */
	public void setBrokerage(long brokerage) {
		this.brokerage = brokerage;
	}



	/**
	 * @return the tax
	 */
	public long getTax() {
		return tax;
	}



	/**
	 * @param tax the tax to set
	 */
	public void setTax(long tax) {
		this.tax = tax;
	}



	/**
	 * @return the fee
	 */
	public long getFee() {
		return fee;
	}



	/**
	 * @param fee the fee to set
	 */
	public void setFee(long fee) {
		this.fee = fee;
	}



	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}



	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}



	/**
	 * @return the beDone
	 */
	public boolean isBeDone() {
		return beDone;
	}



	/**
	 * @param beDone the beDone to set
	 */
	public void setBeDone(boolean beDone) {
		this.beDone = beDone;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TradingRecordDTO [id=").append(id).append(", date=")
				.append(date).append(", market=").append(market)
				.append(", code=").append(code).append(", describe=")
				.append(describe).append(", price=").append(price)
				.append(", vol=").append(vol).append(", amount=")
				.append(amount).append(", brokerage=").append(brokerage)
				.append(", tax=").append(tax).append(", fee=").append(fee)
				.append(", day=").append(day).append(", beDone=")
				.append(beDone).append("]");
		return builder.toString();
	}
	
}
