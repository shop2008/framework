/**
 * 
 */
package com.wxxr.trading.core.storage.transaction;

import com.wxxr.trading.core.storage.api.IBizObjectStorage;
import com.wxxr.trading.core.storage.transaction.persistence.bean.TransactionInfo;

/**
 * @author neillin
 *
 */
public interface ITransactionStorage extends IBizObjectStorage<Long, AbstractTransaction, TransactionInfo> {

}
