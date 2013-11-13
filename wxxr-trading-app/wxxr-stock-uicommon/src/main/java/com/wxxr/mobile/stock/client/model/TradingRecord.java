package com.wxxr.mobile.stock.client.model;


public class TradingRecord {

	private static final long serialVersionUID = 1L;
	private long date;// 日期
	private String market;// 股票市场
	private String code;// 股票代码
	private String describe;// 成交方向：
	private long price;// 成交价格
	private long vol;// 成交量
	private long amount;// 成交金额
	private long brokerage;// 佣金
	private long tax;// 印花税
	private long fee;// 过户费
	private int day;// 1:表示t日,0:表示t+1日
	private boolean beDone;// 订单是否完成，DONE为true；否则为false

	/**
	 * @return the date
	 */

	public long getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
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
	 * @param market
	 *            the market to set
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
	 * @param code
	 *            the code to set
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
	 * @param describe
	 *            the describe to set
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
	 * @param price
	 *            the price to set
	 */
	public void setPrice(long price) {
		this.price = price;
	}

	/**
	 * @return the brokerage
	 */

	public long getBrokerage() {
		return brokerage;
	}

	/**
	 * @return the vol
	 */

	public long getVol() {
		return vol;
	}

	/**
	 * @param vol
	 *            the vol to set
	 */
	public void setVol(long vol) {
		this.vol = vol;
	}

	/**
	 * @return the value
	 */

	public long getAmount() {
		return amount;
	}

	/**
	 * @return the value
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}

	/**
	 * @param brokerage
	 *            the brokerage to set
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
	 * @param tax
	 *            the tax to set
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
	 * @param fee
	 *            the fee to set
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
	 * @param day
	 *            the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * 
	 * @return
	 */

	public boolean isBeDone() {
		return beDone;
	}

	/**
	 * 
	 * @param beDone
	 */
	public void setBeDone(boolean beDone) {
		this.beDone = beDone;
	}

	@Override
	public String toString() {
		return "TradingRecordVO [amount=" + amount + ", beDone=" + beDone
				+ ", brokerage=" + brokerage + ", code=" + code + ", date="
				+ date + ", day=" + day + ", describe=" + describe + ", fee="
				+ fee + ", market=" + market + ", price=" + price + ", tax="
				+ tax + ", vol=" + vol + "]";
	}

}
