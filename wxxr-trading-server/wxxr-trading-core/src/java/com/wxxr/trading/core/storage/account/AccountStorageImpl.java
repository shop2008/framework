/**
 * 
 */
package com.wxxr.trading.core.storage.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wxxr.common.jmx.annotation.ServiceMBean;
import com.wxxr.common.microkernel.IKernelContext;
import com.wxxr.persistence.DAOFactory;
import com.wxxr.stock.common.service.api.IMobileStockAppContext;
import com.wxxr.trading.core.model.ITradingRecord;
import com.wxxr.trading.core.storage.account.bean.AssetAccountInfo;
import com.wxxr.trading.core.storage.account.bean.AssetInfo;
import com.wxxr.trading.core.storage.account.persistence.IAssetAccountDAO;
import com.wxxr.trading.core.storage.api.IDataAccessObject;
import com.wxxr.trading.core.storage.common.AbstractBizObjectStorage;
import com.wxxr.trading.core.storage.record.ITradingRecordStorage;
import com.wxxr.trading.core.storage.record.TradingRecordObject;

/**
 * @author neillin
 *
 */
@ServiceMBean
public class AccountStorageImpl extends AbstractBizObjectStorage<Long, AssetAccount, AssetAccountInfo> implements IAccountStorage {

	private IKernelContext context;
	
	private IAssetStorage assetStorage;
	
	private IAssetAccountDAO acctDAO;
	private ITradingRecordStorage recordStorage;
	
	private Map<String, IAccountOperationHandler> handlers = new HashMap<String, IAccountOperationHandler>();
	private Context storageContext = new Context() {
		
		@Override
		public void saveTradingRecord(ITradingRecord record) {
			getRecordStorage().saveOrUpdate((TradingRecordObject)record);
		}
		
		@Override
		public IAccountStorage getStorage() {
			return AccountStorageImpl.this;
		}
	};

	
	private IDataAccessObject<Long, AssetAccountInfo> baseDAO = new IDataAccessObject<Long, AssetAccountInfo>() {
		
		@Override
		public void update(AssetAccountInfo object) {
			getAccountDAO().update(object);
		}
		
		@Override
		public void remove(AssetAccountInfo object) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean hasQuery(String queryName) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public String getType(AssetAccountInfo object) {
			return object.getType();
		}
		
		@Override
		public String[] getQueryNames() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public AssetAccountInfo findByPrimaryKey(Long key) {
			return getAccountDAO().findByPrimaryKey(key);
		}
		
		@Override
		public List<AssetAccountInfo> doQuery(String name,
				Map<String, Object> criteria) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Long add(AssetAccountInfo object) {
			return getAccountDAO().add(object);
		}
	};
	
	protected IAssetAccountDAO getAccountDAO() {
		if(this.acctDAO == null){
			this.acctDAO = DAOFactory.getDAObject(IAssetAccountDAO.class);
		}
		return this.acctDAO;
	}

	protected IAssetStorage getAssetStorage() {
		if(this.assetStorage == null){
			this.assetStorage = this.context.getService(IAssetStorage.class);
		}
		return this.assetStorage;
	}
	
	protected ITradingRecordStorage getRecordStorage() {
		if(this.recordStorage == null){
			this.recordStorage = DAOFactory.getDAObject(ITradingRecordStorage.class);
		}
		return this.recordStorage;
	}


	@Override
	protected IDataAccessObject<Long, AssetAccountInfo> getBaseDAO() {
		return this.baseDAO;
	}

	@Override
	protected boolean updateBaseDO(AssetAccountInfo info,
			AssetAccount biz) {
		return false;
	}

	@Override
	protected void updateBO(AssetAccount biz, AssetAccountInfo info) {
		biz.setId(info.getId());
		biz.setCreatedBy(info.getCreatedBy());
		biz.setCreatedDate(info.getCreatedDate());
		biz.setLastUpdatedBy(info.getLastUpdatedBy());
		biz.setLastUpdatedDate(info.getLastUpdatedDate());
		biz.setType(info.getType());
		biz.setOwnerId(info.getOwnerId());
		List<AssetInfo> list = info.getAllAssets();
		if(list != null){
			for (AssetInfo f : list) {
				biz.addAsset(getAssetStorage().get(f.getId()));
			}
		}		
		
	}

	@Override
	protected AssetAccountInfo createBaseDO(AssetAccount bizObject) {
		AssetAccountInfo info = new AssetAccountInfo();
		info.setId(bizObject.getId());
		info.setOwnerId(bizObject.getOwnerId());
		info.setType(bizObject.getType());
		return info;
	}

	@Override
	protected String getStorageName() {
		return "Account Storage";
	}
	
	
	
	
	public void startService(IKernelContext ctx){
		this.context = ctx;
		this.context.registerService(IAccountStorage.class, this);
	}
	
	public void stopService(IKernelContext ctx) {
		this.context = null;
		ctx.unregisterService(IAccountStorage.class, this);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.account.IAccountStorage#getAccountOperationHandler(java.lang.String)
	 */
	@Override
	public IAccountOperationHandler getAccountOperationHandler(String acctType) {
		synchronized(this.handlers){
			return this.handlers.get(acctType);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.account.IAccountStorage#registerAccountOperationHandler(java.lang.String, com.wxxr.trading.core.storage.account.IAccountOperationHandler)
	 */
	@Override
	public void registerAccountOperationHandler(String acctType,
			IAccountOperationHandler handler) {
		synchronized(this.handlers){
			this.handlers.put(acctType, handler);
		}
		handler.init(storageContext);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.account.IAccountStorage#unregisterAccountOperationHandler(java.lang.String, com.wxxr.trading.core.storage.account.IAccountOperationHandler)
	 */
	@Override
	public boolean unregisterAccountOperationHandler(String acctType,
			IAccountOperationHandler handler) {
		synchronized(this.handlers){
			IAccountOperationHandler old = this.handlers.get(acctType);
			if(old == handler){
				this.handlers.remove(acctType);
				old.destroy();
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.AbstractModule#registerService(com.wxxr.stock.common.service.api.IMobileStockAppContext)
	 */
	@Override
	protected void registerService(IMobileStockAppContext ctx) {
		ctx.registerService(IAccountStorage.class, this);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.AbstractModule#unregisterService(com.wxxr.stock.common.service.api.IMobileStockAppContext)
	 */
	@Override
	protected void unregisterService(IMobileStockAppContext ctx) {
		ctx.unregisterService(IAccountStorage.class, this);
	}

}
