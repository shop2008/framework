package com.wxxr.mobile.stock.client.biz;

import java.io.Serializable;

public class StockParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 交易盘ID
	 */
	private String accid;
	/**
	 * 模拟盘标记
	 */
	private boolean virtual;
	/**
	 * 用户是否为自己
	 */
	private boolean isself;
	/**
	 * 买人价格
	 * */
	private long buyPrice;
	
	private long amount;
	private int position;
	private String name;
	private String code;
	private String market;
	
	
	
	public boolean getVirtual() {
		return virtual;
	}
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	public boolean getIsself() {
		return isself;
	}
	public void setIsself(boolean isself) {
		this.isself = isself;
	}
	public String getAccid() {
		return accid;
	}
	public void setAccid(String accid) {
		this.accid = accid;
	}

	public long getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(long buyPrice) {
		this.buyPrice = buyPrice;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
}
