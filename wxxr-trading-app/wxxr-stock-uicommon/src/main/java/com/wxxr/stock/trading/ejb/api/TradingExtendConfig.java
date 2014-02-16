package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;


public class TradingExtendConfig implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String key;
	private String value;
	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TradingExtendConfig [key=" + key + ", value=" + value + "]";
	}
	
}
