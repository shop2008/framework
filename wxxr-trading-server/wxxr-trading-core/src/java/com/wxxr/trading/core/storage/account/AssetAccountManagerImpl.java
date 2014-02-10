/**
 * 
 */
package com.wxxr.trading.core.storage.account;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.wxxr.common.microkernel.IKernelContext;
import com.wxxr.persistence.DAOFactory;
import com.wxxr.trading.core.api.IGenericAccountManager;
import com.wxxr.trading.core.api.TradingContext;
import com.wxxr.trading.core.exception.AccountLockFailedException;
import com.wxxr.trading.core.exception.InsufficientAssetException;
import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.IAccountLockStrategy;
import com.wxxr.trading.core.model.IAsset;
import com.wxxr.trading.core.model.IAssetAccount;
import com.wxxr.trading.core.model.IAssetAmount;
import com.wxxr.trading.core.model.IAssetFrozenItem;

/**
 * @author neillin
 *
 */
public class AssetAccountManagerImpl implements
		IGenericAccountManager {
	public static final String ACCOUNT_ID_PREFIX = "LOCK";
	private int accountLockTimeoutInSeconds = 15;		// 最多等15秒， 因为transaction一般会在15秒后过期
	private IAccountLockStrategy lockStrategy;
	private IAssetStorage assetStorage;
	private IAccountStorage accountStorage;
	private IKernelContext context;
	
	
	protected IAccountLockStrategy getLockStrategy() {
		if(this.lockStrategy == null){
			this.lockStrategy = context.getService(IAccountLockStrategy.class);
		}
		return this.lockStrategy;
	}
	
	protected IAccountStorage getAccountStorage() {
		if(this.accountStorage == null){
			this.accountStorage = context.getService(IAccountStorage.class);
		}
		return this.accountStorage;
	}

	protected IAssetStorage getAssetStorage() {
		if(this.assetStorage == null){
			this.assetStorage = context.getService(IAssetStorage.class);
		}
		return this.assetStorage;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.account.IAssetAccountManager#unlockAccount(java.lang.Long)
	 */
	@Override
	public boolean unlockAccount(Long accountId) {
		boolean bool = false;
		TradingContext ctx = TradingContext.current();
		bool = context.getService(IAccountLockStrategy.class).unlockAccount(getAccountKey(accountId));
		if(ctx != null){
			ctx.removeLockAccountId(accountId);
		}
		return bool;
	}
	
	protected String getAccountKey(Long accountId){
		return new StringBuffer(ACCOUNT_ID_PREFIX).append(accountId).toString();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.account.IAssetAccountManager#unfrost(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void unfrost(Long accountId, Long frozenRecordId,String comments) {
		AssetFrozenItem item = getAssetStorage().findFrozenItem(frozenRecordId);
		if(item != null){
			Asset asset = getAssetStorage().get(item.getAssetId());
			getAssetStorage().getAssetOperationHandler(asset.getType()).unfrost(item, comments);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.account.IAssetAccountManager#hasAsset(java.lang.Long, com.wxxr.stock.trading.account.model.AssetType)
	 */
	@Override
	public boolean hasAsset(Long accountId, String assetType, boolean virtual) {
		IAssetAccount acct = getAccount(accountId);
		acct.getAllAssets();
		List<Long> assetIds = acct != null ? acct.getAllAssets() : null;
		if(assetIds != null){
			for (Long id : assetIds) {
				IAsset asset = acct.getAsset(id);
				if((asset != null)&& asset.getType().equals(assetType) && (asset.isVirtual() == virtual)){
					return true;
				}
			}
		}
		return false;
	}
	
	protected Asset findCorrespondentAsset(Long accountId, final IAssetAmount amount) {
		AssetAccount acct = getAccount(accountId);
		acct.getAllAssets();
		List<Long> assetIds = acct != null ? acct.getAllAssets() : null;
		if(assetIds != null){
			for (Long id : assetIds) {
				Asset asset = acct.getAsset(id);
				if((asset != null)&& asset.isMyAmount(amount)){
					return asset;
				}
			}
		}
		return null;
	}

	@Override
	public void credit(Long accountId, IAssetAmount amount,String comments) throws TradingException {
		if(amount.getAmount() < 0L){
			throw new IllegalArgumentException("Credit amount cannot be negative :["+amount+"]");
		}
		lockAccount(accountId);
		Asset asset = findCorrespondentAsset(accountId, amount);
		if(asset == null){
			getAssetStorage().getAssetOperationHandler(amount.getAssetType()).newAsset(amount, comments);
		}else{
			getAssetStorage().getAssetOperationHandler(amount.getAssetType()).credit(asset, amount, comments);
		}
		
	}

	@Override
	public void debit(Long accountId, IAssetAmount amount,String comments)
			throws TradingException {
		if(amount.getAmount() < 0L){
			throw new IllegalArgumentException("Debit amount cannot be negative :["+amount+"]");
		}
		lockAccount(accountId);
		Asset asset = findCorrespondentAsset(accountId, amount);
		if(asset == null){
			throw new InsufficientAssetException("Cannot find asset of :["+amount.getAssetType()+"/"+(amount.isVirtual() ? "VIRTUAL" : "REGULAR")+"] in account :"+accountId);
		}else{
			getAssetStorage().getAssetOperationHandler(amount.getAssetType()).debit(asset, amount, comments);
		}
		
	}

	@Override
	public Long freeze(Long accountId, IAssetAmount amount,String comments)
			throws TradingException {
		if(amount.getAmount() < 0L){
			throw new IllegalArgumentException("Freezing amount cannot be negative :["+amount+"]");
		}
		lockAccount(accountId);
		Asset asset = findCorrespondentAsset(accountId, amount);
		if(asset == null){
			throw new InsufficientAssetException("Cannot find asset of :["+amount.getAssetType()+"/"+(amount.isVirtual() ? "VIRTUAL" : "REGULAR")+"] in account :"+accountId);
		}else{
			return getAssetStorage().getAssetOperationHandler(amount.getAssetType()).freeze(asset, amount, comments);
		}
		
	}

	@Override
	public void debitFrozen(Long accountId, Long frozenRecordId,
			IAssetAmount amount,String comments) throws TradingException {
		if(amount.getAmount() < 0L){
			throw new IllegalArgumentException("Debit amount cannot be negative :["+amount+"]");
		}
		lockAccount(accountId);
		AssetFrozenItem item = getAssetStorage().findFrozenItem(frozenRecordId);
		if((item == null)||item.isClosed()){
			throw new InsufficientAssetException("Cannot find frozen asset of :["+amount.getAssetType()+"/"+(amount.isVirtual() ? "VIRTUAL" : "REGULAR")+"] in account :"+accountId);
		}
		getAssetStorage().getAssetOperationHandler(amount.getAssetType()).debitFromFrozen(item, amount, comments);
	}


	@Override
	public void addNewAsset(Long accountId,IAssetAmount amount,String comments) throws TradingException {
		if(amount.getAmount() < 0L){
			throw new IllegalArgumentException("Credit amount cannot be negative :["+amount+"]");
		}
		lockAccount(accountId);
		Asset asset = findCorrespondentAsset(accountId, amount);
		if(asset != null){
			throw new IllegalArgumentException("Asset with type :"+amount.getAssetType()+" has already existed !");
		}
		getAssetStorage().getAssetOperationHandler(amount.getAssetType()).newAsset(amount, comments);
	}
	

	@Override
	public AssetAccount getAccount(Long accountId) {
		return getAccountStorage().get(accountId);
	}

	@Override
	public void lockAccount(Long accountId) throws AccountLockFailedException {
		TradingContext ctx = TradingContext.current();
		if((ctx != null)&&(ctx.isAccountLocked(accountId) == false)){
			if(!context.getService(IAccountLockStrategy.class).lockAccount(getAccountKey(accountId), this.accountLockTimeoutInSeconds, TimeUnit.SECONDS)){
				throw new AccountLockFailedException("Failed to lock account :"+accountId+" within "+this.accountLockTimeoutInSeconds+" seconds !");
			}
			ctx.addLockAccountId(accountId);
		}else if(ctx == null){
			throw new AccountLockFailedException("TradingContext is required to lock account :"+accountId);
		}
	}
	
	protected <T> T getDAO(Class<T> clazz) {
		return DAOFactory.getDAObject(clazz);
	}

	@Override
	public void checkIfHasSufficientAsset4Debit(Long accountId,
			IAssetAmount amount) throws TradingException {
		if(amount.getAmount() < 0L){
			return;
		}
		lockAccount(accountId);
		Asset asset = findCorrespondentAsset(accountId, amount);
		if(asset == null){
			throw new InsufficientAssetException("Cannot find asset of :["+amount.getAssetType()+"/"+(amount.isVirtual() ? "VIRTUAL" : "REGULAR")+"] in account :"+accountId);
		}else{
			long usableBalance = asset.getBalance() - asset.getFrozenAmount();
			if(usableBalance < amount.getAmount()){
				throw new InsufficientAssetException("There is not enough fund for debit :["+amount+"] in account :"+accountId);
			}
		}		
	}

	@Override
	public Asset getAssetByFrozenId(Long frozenId) {
		AssetFrozenItem item = getAssetStorage().findFrozenItem(frozenId);
		if(item != null){
			return getAssetStorage().get(item.getAssetId());
		}
		return null;
	}

	@Override
	public IAssetFrozenItem getAssetFrozenItemByFrozenId(Long frozenId) {
		return getAssetStorage().findFrozenItem(frozenId);
	}

	public void startService(IKernelContext ctx){
		this.context = ctx;
		this.context.registerService(IGenericAccountManager.class, this);
	}
	
	public void stopService(IKernelContext ctx) {
		this.context = null;
		ctx.unregisterService(IGenericAccountManager.class, this);
	}

	@Override
	public IAssetAccount openAccount(String ownerId, String acctType,
			Map<String, Object> params) {
		IAccountOperationHandler handler = getAccountStorage().getAccountOperationHandler(acctType);
		Long acctId = handler.openAccount(ownerId, acctType, params);
		return getAccount(acctId);
	}

	@Override
	public void closeAccount(Long accountId) {
		IAssetAccount acct = getAccount(accountId);
		IAccountOperationHandler handler = getAccountStorage().getAccountOperationHandler(acct.getType());
		handler.closeAccount(acct);
	}
	
}
