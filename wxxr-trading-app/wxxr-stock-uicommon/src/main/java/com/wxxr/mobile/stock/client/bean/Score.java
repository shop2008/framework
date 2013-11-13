package com.wxxr.mobile.stock.client.bean;

/**
 * 用户积分实体
 *
 */
public class Score {

	/**
	 * 积分类别
	 */
	private String catagory;
	
	/**
	 * 积分获取日期
	 */
	private String date;
	
	/**
	 * 积分获得数量
	 */
	private String amount;
	
	
	public String getCatagory() {
		return catagory;
	}

	public void setCatagory(String catagory) {
		this.catagory = catagory;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
