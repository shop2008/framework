/**
 * 
 */
package com.wxxr.trading.core.storage.account;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.trading.core.exception.InsufficientAssetException;
import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.AccountOperation;
import com.wxxr.trading.core.model.IAssetAmount;
import com.wxxr.trading.core.model.ITradingRecord;
import com.wxxr.trading.core.storage.account.IAssetStorage.Context;

/**
 * @author neillin
 *
 */
public abstract class AbstractAssetOperationHandler implements
		IAssetOperationHandler {

	private IAssetStorage.Context storageCtx;
	
	
	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.IAssetOperationHandler#debit(com.wxxr.trading.core.storage.account.Asset, com.wxxr.trading.core.storage.account.AssetAmount)
	 */
	@Override
	public void debit(Asset asset, IAssetAmount amount,String comments)
			throws TradingException {
		if(!asset.isMyAmount(amount)){
			throw new IllegalArgumentException("Asset amount :["+amount+"] is not acceptable for asset :"+asset);
		}
		if(amount.getAmount() < 0L){
			throw new IllegalArgumentException("Debit amount cannot be negative :["+amount+"]");
		}
		long prevBalance = asset.getBalance();
		long usableBalance = asset.getBalance() - asset.getFrozenAmount();
		if(usableBalance < amount.getAmount()){
			throw new InsufficientAssetException("There is not enough fund for debit :["+amount+"] in asset :"+asset);
		}
		asset.setBalance(prevBalance-amount.getAmount());
		storageCtx.getStorage().saveOrUpdate(asset);
		storageCtx.saveTradingRecord(createTradingRecord(asset, null,amount, AccountOperation.DEBIT_ASSET,comments));
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.IAssetOperationHandler#credit(com.wxxr.trading.core.storage.account.Asset, com.wxxr.trading.core.storage.account.AssetAmount)
	 */
	@Override
	public void credit(Asset asset, IAssetAmount amount,String comments)
			throws TradingException {
		if(!asset.isMyAmount(amount)){
			throw new IllegalArgumentException("Asset amount :["+amount+"] is not acceptable for asset :"+asset);
		}
		if(amount.getAmount() < 0L){
			throw new IllegalArgumentException("Credit amount cannot be negative :["+amount+"]");
		}
		long prevBalance = asset.getBalance();
		asset.setBalance(prevBalance+amount.getAmount());
		storageCtx.getStorage().saveOrUpdate(asset);
		storageCtx.saveTradingRecord(createTradingRecord(asset, null,amount, AccountOperation.CREDIT_ASSET,comments));		
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.IAssetOperationHandler#debitFromFrozen(com.wxxr.trading.core.storage.account.Asset, com.wxxr.trading.core.storage.account.AssetAmount)
	 */
	@Override
	public void debitFromFrozen(AssetFrozenItem info, IAssetAmount amount,String comments)
			throws TradingException {
		Asset asset = storageCtx.getStorage().get(info.getAssetId());
		if(!asset.isMyAmount(amount)){
			throw new IllegalArgumentException("Asset amount :["+amount+"] is not acceptable for asset :"+asset);
		}
		if(amount.getAmount() < 0L){
			throw new IllegalArgumentException("Debit amount cannot be negative :["+amount+"]");
		}
		if((info == null)||info.isClosed()){
			throw new InsufficientAssetException("Cannot find frozen asset of :["+amount.getAssetType()+"/"+(amount.isVirtual() ? "VIRTUAL" : "REGULAR"));
		}
		long prevBalance = info.getBalance();
		if(prevBalance < amount.getAmount()){
			throw new InsufficientAssetException("There is not enough frozen fund to debit amount :["+amount+"] in frozen item :"+info);
		}
		info.setBalance(prevBalance-amount.getAmount());
		storageCtx.getStorage().updateFrozenItem(info);
		prevBalance = asset.getBalance();
		asset.setBalance(asset.getBalance()-amount.getAmount());
		storageCtx.getStorage().saveOrUpdate(asset);
		storageCtx.saveTradingRecord(createTradingRecord(asset, info, amount, AccountOperation.DEBIT_FROZEN_ASSET,comments));
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.IAssetOperationHandler#freeze(com.wxxr.trading.core.storage.account.Asset, com.wxxr.trading.core.storage.account.AssetAmount)
	 */
	@Override
	public Long freeze(Asset asset, IAssetAmount amount,String comments) throws TradingException {
		if(!asset.isMyAmount(amount)){
			throw new IllegalArgumentException("Asset amount :["+amount+"] is not acceptable for asset :"+asset);
		}
		if(amount.getAmount() < 0L){
			throw new IllegalArgumentException("Freezing amount cannot be negative :["+amount+"]");
		}
		long usableBalance = asset.getBalance() - asset.getFrozenAmount();
		if(usableBalance < amount.getAmount()){
			throw new InsufficientAssetException("There is not enough fund to freeze amount :["+amount+"] in asset :"+asset);
		}
		AssetFrozenItem info = new AssetFrozenItem();
		info.setAssetId(asset.getId());
		info.setBalance(amount.getAmount());
		info.setComments(comments);
		info.setTotalAmount(amount.getAmount());
		Long key = storageCtx.getStorage().addFrozenItem(info);
		storageCtx.saveTradingRecord(createTradingRecord(asset, null, amount, AccountOperation.FREEZE_ASSET,comments));
		return key;

	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.IAssetOperationHandler#unfrost(com.wxxr.trading.core.storage.account.AssetFrozenItem)
	 */
	@Override
	public void unfrost(AssetFrozenItem frozenItem,String comments) {
		Asset asset = storageCtx.getStorage().get(frozenItem.getAssetId());
		long frozenBalance = frozenItem.getBalance();
		frozenItem.setBalance(0L);
		frozenItem.setClosed(true);
		storageCtx.getStorage().updateFrozenItem(frozenItem);
		storageCtx.saveTradingRecord(createTradingRecord(asset, frozenItem, asset.createAssetAmount(frozenBalance), AccountOperation.UNFROST_ASSET,comments));
	}
	
	protected abstract ITradingRecord createTradingRecord(Asset asset, AssetFrozenItem frozenItem,IAssetAmount amount, AccountOperation operation,String comments);

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.api.IAssetOperationHandler#newAsset(com.wxxr.trading.core.storage.account.AssetAmount, java.lang.String)
	 */
	@Override
	public Long newAsset(IAssetAmount amount, String comments)
			throws TradingException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", amount);
		Asset asset = storageCtx.getStorage().newObject(amount.getAssetType(), params);
		Long key = storageCtx.getStorage().saveOrUpdate(asset);
		storageCtx.saveTradingRecord(createTradingRecord(asset, null,amount, AccountOperation.NEW_ASSET,comments));	
		return key;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.account.IAssetOperationHandler#init(com.wxxr.trading.core.storage.account.IAssetStorage.Context)
	 */
	@Override
	public void init(Context ctx) {
		this.storageCtx = ctx;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.trading.core.storage.account.IAssetOperationHandler#destroy()
	 */
	@Override
	public void destroy() {
		this.storageCtx = null;
	}
	
}
