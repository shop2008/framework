/**
 * 
 */
package com.wxxr.trading.core.storage.tradingcode.persistence.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wxxr.persistence.annotation.Persistence;
import com.wxxr.smbiz.bizobject.INumberKeyObject;
import com.wxxr.trading.core.storage.trading.persistence.ITradingInfoDAO;

/**
 * @author neillin
 *
 */
@Entity
@Table(name="TRADING_CODE_INFO")
@Persistence(daoClass = ITradingInfoDAO.class)
@SequenceGenerator(name = "SEQ_TRADING_CODE_ID", sequenceName = "SEQ_TRADING_CODE_ID",initialValue=100)
public class TradingCodeInfo  implements INumberKeyObject<Integer>{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "SEQ_TRADING_CODE_ID")
	private Integer id;
	@Column(name="CODE",length=50)
	private String code;
	@Column(name="DESCRIPTION",length=255)
	private String description;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
