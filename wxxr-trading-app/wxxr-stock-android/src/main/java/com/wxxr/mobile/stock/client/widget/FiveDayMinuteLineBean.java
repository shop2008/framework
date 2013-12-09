package com.wxxr.mobile.stock.client.widget;

public class FiveDayMinuteLineBean {

	float highPrice;
	float lowPrice;
	float maxSecuvolume;
	
	public FiveDayMinuteLineBean(float highPrice, float lowPrice, float maxSecuvolume){
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.maxSecuvolume = maxSecuvolume;
	}

	public float getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(float highPrice) {
		this.highPrice = highPrice;
	}

	public float getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(float lowPrice) {
		this.lowPrice = lowPrice;
	}

	public float getMaxSecuvolume() {
		return maxSecuvolume;
	}

	public void setMaxSecuvolume(float maxSecuvolume) {
		this.maxSecuvolume = maxSecuvolume;
	}
	
	
}
