/**
 * 
 */
package com.wxxr.trading.core.model;

import java.util.Date;

import com.wxxr.trading.core.storage.api.InheritableBizObject;

/**
 * ½»Ò×¼ÇÂ¼
 * @author neillin
 *
 */
public interface ITradingRecord extends InheritableBizObject<Long>{
	Long getId();
	Long getAccountId();
	Long getTradingId();
	Long getTransactionId();
	String getOperator();
	Date getTimestamp();
	String getComments();
	AccountOperation getOperation();
	String getExtraInfo();
	boolean isVirtual();
}
