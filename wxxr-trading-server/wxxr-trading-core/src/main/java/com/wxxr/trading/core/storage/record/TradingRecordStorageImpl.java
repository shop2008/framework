/**
 * 
 */
package com.wxxr.trading.core.storage.record;

import java.util.List;
import java.util.Map;

import com.wxxr.common.jmx.annotation.ServiceMBean;
import com.wxxr.common.microkernel.IKernelContext;
import com.wxxr.persistence.DAOFactory;
import com.wxxr.trading.core.storage.account.persistence.IAssetAccountDAO;
import com.wxxr.trading.core.storage.api.IDataAccessObject;
import com.wxxr.trading.core.storage.common.AbstractBizObjectStorage;
import com.wxxr.trading.core.storage.record.persistence.ITradingRecordDAO;
import com.wxxr.trading.core.storage.record.persistence.bean.TradingRecordInfo;
import com.wxxr.trading.core.storage.trading.persistence.ITradingInfoDAO;
import com.wxxr.trading.core.storage.transaction.persistence.ITransactionInfoDAO;

/**
 * @author neillin
 *
 */
@ServiceMBean
public class TradingRecordStorageImpl extends AbstractBizObjectStorage<Long, TradingRecordObject, TradingRecordInfo> implements ITradingRecordStorage {

	private ITradingRecordDAO recordDAO;
	private IAssetAccountDAO acctDAO;
	private ITradingInfoDAO tradingDAO;
	private ITransactionInfoDAO transactionDAO;
	private IKernelContext context;

	private IDataAccessObject<Long, TradingRecordInfo> baseDAO = new IDataAccessObject<Long, TradingRecordInfo>() {
		
		@Override
		public void update(TradingRecordInfo object) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void remove(TradingRecordInfo object) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean hasQuery(String queryName) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public String getType(TradingRecordInfo object) {
			return object.getOperation().name();
		}
		
		@Override
		public String[] getQueryNames() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public TradingRecordInfo findByPrimaryKey(Long key) {
			return getRecordDAO().findByPrimaryKey(key);
		}
		
		@Override
		public List<TradingRecordInfo> doQuery(String name,
				Map<String, Object> criteria) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Long add(TradingRecordInfo object) {
			return getRecordDAO().add(object);
		}
	};
	
	protected ITradingRecordDAO getRecordDAO() {
		if(this.recordDAO == null){
			this.recordDAO = DAOFactory.getDAObject(ITradingRecordDAO.class);
		}
		return this.recordDAO;
	}

	protected IAssetAccountDAO getAccountDAO() {
		if(this.acctDAO == null){
			this.acctDAO = DAOFactory.getDAObject(IAssetAccountDAO.class);
		}
		return this.acctDAO;
	}
	
	protected ITradingInfoDAO getTradingInfoDAO() {
		if(this.tradingDAO == null){
			this.tradingDAO = DAOFactory.getDAObject(ITradingInfoDAO.class);
		}
		return this.tradingDAO;
	}

	
	protected ITransactionInfoDAO getTransactionDAO() {
		if(this.transactionDAO == null){
			this.transactionDAO = DAOFactory.getDAObject(ITransactionInfoDAO.class);
		}
		return this.transactionDAO;
	}


	
	@Override
	protected IDataAccessObject<Long, TradingRecordInfo> getBaseDAO() {
		return this.baseDAO;
	}

	@Override
	protected boolean updateBaseDO(TradingRecordInfo baseDO,
			TradingRecordObject bizObject) {
		return false;
	}

	@Override
	protected void updateBO(TradingRecordObject biz,
			TradingRecordInfo info) {
		biz.setAccountId(info.getAccount().getId());
		biz.setComments(info.getComments());
		biz.setExtraInfo(info.getExtraInfo());
		biz.setOperation(info.getOperation());
		biz.setOperator(info.getOperator());
		biz.setTimestamp(info.getTimestamp());
		biz.setVirtual(info.isVirtual());
		biz.setTradingId(info.getTrading().getId());
		biz.setTransactionId(info.getTransaction().getId());
	}

	@Override
	protected TradingRecordInfo createBaseDO(TradingRecordObject biz) {
		TradingRecordInfo info = new TradingRecordInfo();
		info.setAccount(getAccountDAO().findByPrimaryKey(biz.getAccountId()));
		info.setComments(biz.getComments());
		info.setExtraInfo(biz.getExtraInfo());
		info.setOperation(biz.getOperation());
		info.setOperator(biz.getOperator());
		info.setTimestamp(biz.getTimestamp());
		info.setVirtual(biz.isVirtual());
		info.setTrading(getTradingInfoDAO().findByPrimaryKey(biz.getTradingId()));
		info.setTransaction(getTransactionDAO().findByPrimaryKey(biz.getTransactionId()));
		return info;
	}

	@Override
	protected String getStorageName() {
		return "Trading Record Storage";
	}
	
	public void startService(IKernelContext ctx){
		this.context = ctx;
		this.context.registerService(ITradingRecordStorage.class, this);
	}
	
	public void stopService(IKernelContext ctx) {
		this.context = null;
		ctx.unregisterService(ITradingRecordStorage.class, this);
	}

	public void start(IKernelContext ctx) {
		ctx.registerService(ITradingRecordStorage.class, this);
	}

	public void stop(IKernelContext ctx) {
		ctx.unregisterService(ITradingRecordStorage.class, this);
	}


	
}
