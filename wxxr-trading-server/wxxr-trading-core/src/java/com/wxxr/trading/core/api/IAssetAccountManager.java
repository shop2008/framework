/**
 * 
 */
package com.wxxr.trading.core.api;

import java.util.Map;

import com.wxxr.trading.core.exception.AccountLockFailedException;
import com.wxxr.trading.core.exception.InsufficientAssetException;
import com.wxxr.trading.core.exception.TradingException;
import com.wxxr.trading.core.model.IAsset;
import com.wxxr.trading.core.model.IAssetAccount;
import com.wxxr.trading.core.model.IAssetAmount;
import com.wxxr.trading.core.model.IAssetFrozenItem;

/**
 * @author neillin
 *
 */
public interface IAssetAccountManager {
	/**
	 * 锁定指定账户，如果失败抛AccountLockFailedException。如果指定账户已经被当前线程锁定，则不要采取任何动作，返回。如果指定账户已被其他线程锁定，
	 * 并且等待一段指定的时间仍未能锁定成功，抛AccountLockFailedException。
	 * @param accountId
	 * @return
	 */
	void lockAccount(Long accountId) throws AccountLockFailedException;
	
	/**
	 * 解除对指定账户的锁定，如果未锁定指定账户，则不要采取任何动作，返回false，否则解冻锁定，返回true
	 * @param accountId
	 * @return
	 */
	boolean unlockAccount(Long accountId);
	
	/**
	 * 贷入/收入资产， 如果实际账户中没有IAssetAmount中的资产类型，抛IllgalArgumentException.
	 * 如果操作成功，必须生成交易记录
	 * @param accountId
	 * @param amount
	 */
	void credit(Long accountId, IAssetAmount amount,String comments) throws TradingException;
	
	/**
	 * 借出/支出资产。如果实际账户中没有AssetAmount中的资产类型，抛IllgalArgumentException，如果账户中没有足够的资产，抛InsufficientAssetException
	 * 如果操作成功，必须生成交易记录
	 * @param accountId
	 * @param amount
	 * @throws InsufficientAssetException
	 */
	void debit(Long accountId, IAssetAmount amount,String comments) throws TradingException;
	
	/**
	 * 冻结资产。如果实际账户中没有AssetAmount中的资产类型，抛IllgalArgumentException，如果账户中没有足够的资产，抛InsufficientAssetException
	 * 如果操作成功，必须生成交易记录,并返回资产冻结记录的id。
	 * @param accountId
	 * @param amount
	 * @throws InsufficientAssetException
	 */
	Long freeze(Long accountId, IAssetAmount amount,String comments) throws TradingException;

	/**
	 * 解除资产冻结。找到指定Id的资产冻结记录，解除记录中资产余额的冻结。如果实际账户中没有指定的资产冻结记录，抛IllgalArgumentException。
	 * 如果操作成功，必须生成交易记录
	 * @param accountId
	 * @param amount
	 * @throws InsufficientAssetException
	 */
	void unfrost(Long accountId, Long frozenRecordId,String comments) throws TradingException;

	/**
	 * 借出/付出冻结中的资产。如果没有找到指定Id的资产冻结记录，或者冻结记录中的资产类型不是指定的资产类型，抛IllgalArgumentException。
	 * 如果冻结记录中资产余额不足，抛InsufficientAssetException
	 * 如果操作成功，必须生成交易记录
	 * @param accountId
	 * @param amount
	 * @throws InsufficientAssetException
	 */
	void debitFrozen(Long accountId, Long frozenRecordId, IAssetAmount amount,String comments) throws TradingException;
	
	/**
	 * 检测指定的账户是否有足够的资产余额支付指定的资产数量，如果没有足够的余额，抛InsufficientAssetException
	 * @param accountId
	 * @param amount
	 * @throws TradingException
	 */
	void checkIfHasSufficientAsset4Debit(Long accountId, IAssetAmount amount) throws TradingException;
	/**
	 * 返回对应accountId的资产账户
	 * @param accountId
	 * @return
	 */
	IAssetAccount getAccount(Long accountId);
	/**
	 * 在指定资产账户中加入新的资产
	 * @param accountId
	 * @param assetType
	 */
	void addNewAsset(Long accountId,IAssetAmount amount,String comments) throws TradingException;
	
	/**
	 * 确定指定的资产账户是否有指定类型的资产
	 * @param accountId
	 * @param assetType
	 * @param virtual
	 * @return
	 */
	boolean hasAsset(Long accountId,String assetType, boolean virtual);
	/**
	 * 
	 * @param frozenId 冻结记录id
	 * @return 冻结的资产
	 */
	IAsset getAssetByFrozenId(Long frozenId);
	/**
	 * 根据冻结ID，取得冻结信息
	 * @param frzenId
	 * @return
	 */
	IAssetFrozenItem getAssetFrozenItemByFrozenId(Long frzenId);
	
	/**
	 * 为指定用户开设新账户
	 * @param ownerId
	 * @param acctType
	 * @param params
	 * @return
	 */
	IAssetAccount openAccount(String ownerId, String acctType, Map<String, Object> params);
	
	/**
	 * 关闭指定账户
	 * @param accountId
	 */
	void closeAccount(Long accountId);
	
}
