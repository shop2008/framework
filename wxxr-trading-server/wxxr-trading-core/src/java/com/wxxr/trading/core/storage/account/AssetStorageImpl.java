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
import com.wxxr.trading.core.storage.account.bean.AssetInfo;
import com.wxxr.trading.core.storage.account.bean.FrozenAssetInfo;
import com.wxxr.trading.core.storage.account.persistence.IAssetAccountDAO;
import com.wxxr.trading.core.storage.account.persistence.IAssetDAO;
import com.wxxr.trading.core.storage.account.persistence.IFrozenAssetDAO;
import com.wxxr.trading.core.storage.api.IDataAccessObject;
import com.wxxr.trading.core.storage.common.AbstractBizObjectStorage;
import com.wxxr.trading.core.storage.record.ITradingRecordStorage;
import com.wxxr.trading.core.storage.record.TradingRecordObject;

/**
 * @author neillin
 *
 */
@ServiceMBean
public class AssetStorageImpl extends AbstractBizObjectStorage<Long, Asset, AssetInfo>
		implements IAssetStorage {

	private IKernelContext context;
	private IFrozenAssetDAO frozenDAO;
	private IAssetDAO assetDAO;
	private IAssetAccountDAO acctDAO;
	private ITradingRecordStorage recordStorage;
	
	private Map<String, IAssetOperationHandler> handlers = new HashMap<String, IAssetOperationHandler>();
	private Context storageContext = new Context() {
		
		@Override
		public void saveTradingRecord(ITradingRecord record) {
			getRecordStorage().saveOrUpdate((TradingRecordObject)record);
		}
		
		@Override
		public IAssetStorage getStorage() {
			return AssetStorageImpl.this;
		}
	};
	
	private IDataAccessObject<Long, AssetInfo> baseDAO = new IDataAccessObject<Long, AssetInfo>() {
		
		@Override
		public void update(AssetInfo object) {
			getAssetDAO().update(object);
		}
		
		@Override
		public void remove(AssetInfo object) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean hasQuery(String queryName) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public String getType(AssetInfo object) {
			return object.getType();
		}
		
		@Override
		public String[] getQueryNames() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public AssetInfo findByPrimaryKey(Long key) {
			return getAssetDAO().findByPrimaryKey(key);
		}
		
		@Override
		public List<AssetInfo> doQuery(String name, Map<String, Object> criteria) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Long add(AssetInfo object) {
			return getAssetDAO().add(object);
		}
	};
	
	protected IFrozenAssetDAO getFrozenAssetDAO() {
		if(this.frozenDAO == null){
			this.frozenDAO = DAOFactory.getDAObject(IFrozenAssetDAO.class);
		}
		return this.frozenDAO;
	}
	
	protected IAssetDAO getAssetDAO() {
		if(this.assetDAO == null){
			this.assetDAO = DAOFactory.getDAObject(IAssetDAO.class);
		}
		return this.assetDAO;
	}
	
	protected IAssetAccountDAO getAccountDAO() {
		if(this.acctDAO == null){
			this.acctDAO = DAOFactory.getDAObject(IAssetAccountDAO.class);
		}
		return this.acctDAO;
	}

	protected ITradingRecordStorage getRecordStorage() {
		if(this.recordStorage == null){
			this.recordStorage = DAOFactory.getDAObject(ITradingRecordStorage.class);
		}
		return this.recordStorage;
	}


	@Override
	public void updateFrozenItem(AssetFrozenItem item) {
		if(item.getId() == null){
			throw new IllegalStateException("AssetFrozenItem :"+item+" miss primary key, cannot be updated !");
		}
		FrozenAssetInfo info = getFrozenAssetDAO().findByPrimaryKey(item.getId());
		if(info.isClosed()){
			throw new IllegalStateException("AssetFrozenItem :"+item+" is closed, cannot be updated !");
		}
		info.setBalance(item.getBalance());
		info.setClosed(item.isClosed());
		getFrozenAssetDAO().update(info);
	}

	@Override
	public Long addFrozenItem(AssetFrozenItem item) {
		FrozenAssetInfo info = new FrozenAssetInfo();
		info.setAsset(getAssetDAO().findByPrimaryKey(item.getAssetId()));
		info.setBalance(item.getBalance());
		info.setClosed(item.isClosed());
		info.setComments(item.getComments());
		info.setTotalAmount(item.getTotalAmount());
		return getFrozenAssetDAO().add(info);
	}

	@Override
	public AssetFrozenItem findFrozenItem(Long itemId) {
		FrozenAssetInfo info = getFrozenAssetDAO().findByPrimaryKey(itemId);
		if(info != null){
			AssetFrozenItem item = new AssetFrozenItem();
			item.setAssetId(info.getAsset().getId());
			item.setBalance(info.getBalance());
			item.setClosed(info.isClosed());
			item.setComments(info.getComments());
			item.setCreatedBy(info.getCreatedBy());
			item.setCreatedDate(info.getCreatedDate());
			item.setLastUpdatedBy(info.getLastUpdatedBy());
			item.setLastUpdatedDate(info.getLastUpdatedDate());
			item.setTotalAmount(info.getTotalAmount());
			return item;
		}
		return null;
	}

	@Override
	public IAssetOperationHandler getAssetOperationHandler(String assetType) {
		synchronized(this.handlers){
			return this.handlers.get(assetType);
		}
	}

	@Override
	public void registerAssetOperationHandler(String assetType,
			IAssetOperationHandler handler) {
		synchronized(this.handlers){
			this.handlers.put(assetType, handler);
		}
		handler.init(storageContext);
		
	}

	@Override
	public boolean unregisterAssetOperationHandler(String assetType,
			IAssetOperationHandler handler) {
		synchronized(this.handlers){
			IAssetOperationHandler old = this.handlers.get(assetType);
			if(old == handler){
				this.handlers.remove(assetType);
				old.destroy();
				return true;
			}
		}
		return false;
	}

	@Override
	protected IDataAccessObject<Long, AssetInfo> getBaseDAO() {
		return this.baseDAO;
	}

	@Override
	protected boolean updateBaseDO(AssetInfo info, Asset asset) {
		long old = info.getBalance();
		if(old != asset.getBalance()){
			info.setBalance(asset.getBalance());
			return true;
		}
		return false;
	}
	

	@Override
	protected void updateBO(Asset biz, AssetInfo info) {
		biz.setAccountId(info.getAccount().getId());
		biz.setBalance(info.getBalance());
		biz.setId(info.getId());
		biz.setVirtual(info.isVirtual());
		biz.setCreatedBy(info.getCreatedBy());
		biz.setCreatedDate(info.getCreatedDate());
		biz.setLastUpdatedBy(info.getLastUpdatedBy());
		biz.setLastUpdatedDate(info.getLastUpdatedDate());
		biz.setType(info.getType());
		List<FrozenAssetInfo> list = info.getFrozenRecords();
		if(list != null){
			for (FrozenAssetInfo f : list) {
				AssetFrozenItem item = new AssetFrozenItem();
				item.setAssetId(f.getAsset().getId());
				item.setBalance(f.getBalance());
				item.setClosed(f.isClosed());
				item.setComments(f.getComments());
				item.setCreatedBy(f.getCreatedBy());
				item.setCreatedDate(f.getCreatedDate());
				item.setId(f.getId());
				item.setLastUpdatedBy(f.getLastUpdatedBy());
				item.setLastUpdatedDate(f.getLastUpdatedDate());
				item.setTotalAmount(f.getTotalAmount());
				biz.addFrozenItem(item);
			}
		}		
	}

	@Override
	protected AssetInfo createBaseDO(Asset bizObject) {
		AssetInfo info = new AssetInfo();
		info.setAccount(getAccountDAO().findByPrimaryKey(bizObject.getAccountId()));
		info.setBalance(bizObject.getBalance());
		info.setId(bizObject.getId());
		info.setType(bizObject.getType());
		info.setVirtual(bizObject.isVirtual());
		return info;
	}

	@Override
	protected String getStorageName() {
		return "Account Asset Storage";
	}

	public void startService(IKernelContext ctx){
		this.context = ctx;
		this.context.registerService(IAssetStorage.class, this);
	}
	
	public void stopService(IKernelContext ctx) {
		this.context = null;
		ctx.unregisterService(IAssetStorage.class, this);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.AbstractModule#registerService(com.wxxr.stock.common.service.api.IMobileStockAppContext)
	 */
	@Override
	protected void registerService(IMobileStockAppContext ctx) {
		ctx.registerService(IAssetStorage.class, this);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.common.service.AbstractModule#unregisterService(com.wxxr.stock.common.service.api.IMobileStockAppContext)
	 */
	@Override
	protected void unregisterService(IMobileStockAppContext ctx) {
		ctx.unregisterService(IAssetStorage.class, this);
	}


}
