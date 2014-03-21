/**
 * 
 */
package com.wxxr.trading.core.model;

import java.util.Date;

import com.wxxr.trading.core.storage.account.TxStatus;
import com.wxxr.trading.core.storage.api.InheritableBizObject;


/**
 * @author wangyan
 *
 */
public interface ITradingTransaction extends InheritableBizObject<Long>{
	Long getTradingId();
	String getTransactionCode();
	String getDescription();
	Date getCreatedTime();
	Date getCompletedTime();
	TxStatus getStatus();
}
