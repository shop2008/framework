/**
 * 
 */
package com.wxxr.trading.core.storage.trading.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.wxxr.persistence.annotation.Persistence;
import com.wxxr.trading.core.storage.account.bean.AssetAccountInfo;
import com.wxxr.trading.core.storage.trading.persistence.ITradingPartyDAO;

/**
 * @author neillin
 *
 */
@Entity
@Table(name="T_PARTY_INFO")
@Persistence(daoClass = ITradingPartyDAO.class)
@SequenceGenerator(name = "SEQ_T_PARTY_ID", sequenceName = "SEQ_T_PARTY_ID",initialValue=100)
public class TradingPartyInfo {
	@Id
	@Column(name="ID")
	@GeneratedValue(generator = "SEQ_T_PARTY_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="TRADING_ID",nullable=false)
	private TradingInfo trading;
	
	@OneToOne
	@JoinColumn(name="ACCT_ID",nullable=false)
	private AssetAccountInfo account;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the trading
	 */
	public TradingInfo getTrading() {
		return trading;
	}

	/**
	 * @return the account
	 */
	public AssetAccountInfo getAccount() {
		return account;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param trading the trading to set
	 */
	public void setTrading(TradingInfo trading) {
		this.trading = trading;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(AssetAccountInfo account) {
		this.account = account;
	}
	
	
}
