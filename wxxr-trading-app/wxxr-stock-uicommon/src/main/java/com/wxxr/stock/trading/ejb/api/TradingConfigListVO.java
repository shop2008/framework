package com.wxxr.stock.trading.ejb.api;

import java.io.Serializable;
import java.util.List;

import com.wxxr.javax.xml.bind.annotation.XmlElement;
import com.wxxr.javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement(name = "TradingConfigListVO")
public class TradingConfigListVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "tradingConfig")
	private List<TradingConfigVO> tradingConfig;

	/**
	 * @return the tradingConfig
	 */
	
	public List<TradingConfigVO> getTradingConfig() {
		return tradingConfig;
	}

	/**
	 * @param tradingConfig the tradingConfig to set
	 */
	public void setTradingConfig(List<TradingConfigVO> tradingConfig) {
		this.tradingConfig = tradingConfig;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TradingConfigListVO [tradingConfig=" + tradingConfig + "]";
	}
	
	
}
