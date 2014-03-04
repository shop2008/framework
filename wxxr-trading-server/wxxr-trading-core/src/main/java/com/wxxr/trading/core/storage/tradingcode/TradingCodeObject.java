/**
 * 
 */
package com.wxxr.trading.core.storage.tradingcode;

import com.wxxr.trading.core.model.ITradingCode;

/**
 * @author neillin
 *
 */
public class TradingCodeObject implements ITradingCode {

	private Integer id;
	private String code;
	private String description;
	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.model.ITradingCode#getCodeId()
	 */
	@Override
	public Integer getCodeId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.model.ITradingCode#getCode()
	 */
	@Override
	public String getCode() {
		return code;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.model.ITradingCode#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getType() {
		return code;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id=id;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
}
