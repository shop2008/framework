/**
 * 
 */
package com.wxxr.trading.core.storage.transaction.persistence;

import com.wxxr.persistence.annotation.Command;
import com.wxxr.persistence.service.FindByPrimaryKeyCommand;
import com.wxxr.trading.core.storage.transaction.persistence.bean.TransactionInfo;

/**
 * @author wangyan
 *
 */
public interface ITransactionInfoDAO {
	Long add(TransactionInfo transactionInfo);
	void update(TransactionInfo transactionInfo);
	void remove(TransactionInfo transactionInfo);
	@Command(clazz=FindByPrimaryKeyCommand.class)
	TransactionInfo findByPrimaryKey(Long id);
}
