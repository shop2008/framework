/**
 * 
 */
package com.wxxr.trading.core.storage.transaction;

import java.util.List;
import java.util.Map;

import com.wxxr.persistence.DAOFactory;
import com.wxxr.trading.core.model.TradingStatus;
import com.wxxr.trading.core.storage.account.TxStatus;
import com.wxxr.trading.core.storage.api.IDataAccessObject;
import com.wxxr.trading.core.storage.common.AbstractBizObjectStorage;
import com.wxxr.trading.core.storage.transaction.persistence.ITransactionInfoDAO;
import com.wxxr.trading.core.storage.transaction.persistence.bean.TransactionInfo;

/**
 * @author wangyan
 *
 */
public class TransactionStorageImpl extends AbstractBizObjectStorage<Long,AbstractTransaction,TransactionInfo> implements ITransactionStorage {

	private ITransactionInfoDAO transactionInfoDAO;
	private IDataAccessObject<Long,TransactionInfo> baseDao=new IDataAccessObject<Long,TransactionInfo>(){

		@Override
		public TransactionInfo findByPrimaryKey(Long key) {
			return getTransactionInfoDAO().findByPrimaryKey(key);
		}

		@Override
		public void update(TransactionInfo object) {
			getTransactionInfoDAO().update(object);			
		}

		@Override
		public void remove(TransactionInfo object) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Long add(TransactionInfo object) {
			return getTransactionInfoDAO().add(object);
		}

		@Override
		public String getType(TransactionInfo object) {
			return object.getType();
		}

		@Override
		public String[] getQueryNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasQuery(String queryName) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public List<TransactionInfo> doQuery(String name,
				Map<String, Object> criteria) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	@Override
	protected IDataAccessObject<Long, TransactionInfo> getBaseDAO() {
		return baseDao;
	}

	@Override
	protected boolean updateBaseDO(TransactionInfo baseDO,
			AbstractTransaction bizObject) {
		TxStatus status = baseDO.getStatus();
		if(!isSame(status, bizObject.getStatus())){
			baseDO.setStatus(bizObject.getStatus());
			return true;
		}
		return false;
	}

	protected boolean isSame(Object obj1, Object obj2){
		if((obj1 == null)&&(obj2 == null)){
			return true;
		}
		
		if((obj1 == null)||(obj2 == null)){
			return false;
		}

		return obj1.equals(obj2);
	}
	@Override
	protected void updateBO(AbstractTransaction bizObject,
			TransactionInfo baseDO) {
		bizObject.setCompletedTime(baseDO.getCompletedTime());
		bizObject.setCreatedTime(baseDO.getCreatedTime());
		bizObject.setDescription(baseDO.getDescription());
		bizObject.setId(baseDO.getId());
		bizObject.setOperationCode(baseDO.getTransactionCode());
		bizObject.setStatus(baseDO.getStatus());
		bizObject.setTradingId(baseDO.getTradingId());
		bizObject.setType(baseDO.getType());
	}

	@Override
	protected TransactionInfo createBaseDO(AbstractTransaction bizObject) {
		TransactionInfo transactionInfo=new TransactionInfo();
		
		transactionInfo.setCompletedTime(bizObject.getCompletedTime());
		transactionInfo.setCreatedTime(bizObject.getCreatedTime());
		transactionInfo.setDescription(bizObject.getDescription());
		transactionInfo.setTransactionCode(bizObject.getOperationCode());
		transactionInfo.setStatus(bizObject.getStatus());
		transactionInfo.setTradingId(bizObject.getTradingId());
		transactionInfo.setType(bizObject.getType());
		return transactionInfo;
	}

	@Override
	protected String getStorageName() {
		return "Transaction Info Object Storage";
	}

	/**
	 * @return the transactionInfoDAO
	 */
	public ITransactionInfoDAO getTransactionInfoDAO() {
		if(transactionInfoDAO==null){
			transactionInfoDAO=DAOFactory.getDAObject(ITransactionInfoDAO.class);
		}
		return transactionInfoDAO;
	}

	
}
