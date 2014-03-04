/**
 * 
 */
package com.wxxr.trading.core.model;

import java.util.Date;

import com.wxxr.trading.core.storage.api.InheritableBizObject;


/**
 * 交易
 * @author wangyan
 *
 */
public interface ITrading extends InheritableBizObject<Long> {
	/**
	 * 用户
	 * @return
	 */
	String getOwnerId();
	/**
	 * 经纪人
	 * @return
	 */
	String getBroker();
	/**
	 * 交易类型
	 * @return
	 */
	Integer getTradingCode();
	/**
	 * 交易状态
	 * @return
	 */
	TradingStatus getStatus();


	/**
	 * 交易账户id
	 * @return
	 */
	Long[] getTradingParties();
	
	
	String getSubStatus();
	
	Date getCreatedTime();
	
	Date getCompletedTime();
}
