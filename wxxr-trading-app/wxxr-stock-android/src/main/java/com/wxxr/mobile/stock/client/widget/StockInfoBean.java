package com.wxxr.mobile.stock.client.widget;

public class StockInfoBean {

	private String name;
	private String code;
	private String market;
	private boolean isSelected = false; 
	
	public StockInfoBean(){
		
	}
	
	public StockInfoBean(String code, String market, String name){
		this.code = code;
		this.market = market;
		this.name = name;
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

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
