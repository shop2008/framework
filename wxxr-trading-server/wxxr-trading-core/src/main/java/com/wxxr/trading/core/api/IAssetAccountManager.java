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
	 * ����ָ���˻������ʧ����AccountLockFailedException�����ָ���˻��Ѿ�����ǰ�߳���������Ҫ��ȡ�κζ��������ء����ָ���˻��ѱ������߳�������
	 * ���ҵȴ�һ��ָ����ʱ����δ�������ɹ�����AccountLockFailedException��
	 * @param accountId
	 * @return
	 */
	void lockAccount(Long accountId) throws AccountLockFailedException;
	
	/**
	 * �����ָ���˻������������δ����ָ���˻�����Ҫ��ȡ�κζ���������false������ⶳ����������true
	 * @param accountId
	 * @return
	 */
	boolean unlockAccount(Long accountId);
	
	/**
	 * ����/�����ʲ��� ���ʵ���˻���û��IAssetAmount�е��ʲ����ͣ���IllgalArgumentException.
	 * ��������ɹ����������ɽ��׼�¼
	 * @param accountId
	 * @param amount
	 */
	void credit(Long accountId, IAssetAmount amount,String comments) throws TradingException;
	
	/**
	 * ���/֧���ʲ������ʵ���˻���û��AssetAmount�е��ʲ����ͣ���IllgalArgumentException������˻���û���㹻���ʲ�����InsufficientAssetException
	 * ��������ɹ����������ɽ��׼�¼
	 * @param accountId
	 * @param amount
	 * @throws InsufficientAssetException
	 */
	void debit(Long accountId, IAssetAmount amount,String comments) throws TradingException;
	
	/**
	 * �����ʲ������ʵ���˻���û��AssetAmount�е��ʲ����ͣ���IllgalArgumentException������˻���û���㹻���ʲ�����InsufficientAssetException
	 * ��������ɹ����������ɽ��׼�¼,�������ʲ������¼��id��
	 * @param accountId
	 * @param amount
	 * @throws InsufficientAssetException
	 */
	Long freeze(Long accountId, IAssetAmount amount,String comments) throws TradingException;

	/**
	 * ����ʲ����ᡣ�ҵ�ָ��Id���ʲ������¼�������¼���ʲ����Ķ��ᡣ���ʵ���˻���û��ָ�����ʲ������¼����IllgalArgumentException��
	 * ��������ɹ����������ɽ��׼�¼
	 * @param accountId
	 * @param amount
	 * @throws InsufficientAssetException
	 */
	void unfrost(Long accountId, Long frozenRecordId,String comments) throws TradingException;

	/**
	 * ���/���������е��ʲ������û���ҵ�ָ��Id���ʲ������¼�����߶����¼�е��ʲ����Ͳ���ָ�����ʲ����ͣ���IllgalArgumentException��
	 * ��������¼���ʲ����㣬��InsufficientAssetException
	 * ��������ɹ����������ɽ��׼�¼
	 * @param accountId
	 * @param amount
	 * @throws InsufficientAssetException
	 */
	void debitFrozen(Long accountId, Long frozenRecordId, IAssetAmount amount,String comments) throws TradingException;
	
	/**
	 * ���ָ�����˻��Ƿ����㹻���ʲ����֧��ָ�����ʲ����������û���㹻������InsufficientAssetException
	 * @param accountId
	 * @param amount
	 * @throws TradingException
	 */
	void checkIfHasSufficientAsset4Debit(Long accountId, IAssetAmount amount) throws TradingException;
	/**
	 * ���ض�ӦaccountId���ʲ��˻�
	 * @param accountId
	 * @return
	 */
	IAssetAccount getAccount(Long accountId);
	/**
	 * ��ָ���ʲ��˻��м����µ��ʲ�
	 * @param accountId
	 * @param assetType
	 */
	void addNewAsset(Long accountId,IAssetAmount amount,String comments) throws TradingException;
	
	/**
	 * ȷ��ָ�����ʲ��˻��Ƿ���ָ�����͵��ʲ�
	 * @param accountId
	 * @param assetType
	 * @param virtual
	 * @return
	 */
	boolean hasAsset(Long accountId,String assetType, boolean virtual);
	/**
	 * 
	 * @param frozenId �����¼id
	 * @return ������ʲ�
	 */
	IAsset getAssetByFrozenId(Long frozenId);
	/**
	 * ���ݶ���ID��ȡ�ö�����Ϣ
	 * @param frzenId
	 * @return
	 */
	IAssetFrozenItem getAssetFrozenItemByFrozenId(Long frzenId);
	
	/**
	 * Ϊָ���û��������˻�
	 * @param ownerId
	 * @param acctType
	 * @param params
	 * @return
	 */
	IAssetAccount openAccount(String ownerId, String acctType, Map<String, Object> params);
	
	/**
	 * �ر�ָ���˻�
	 * @param accountId
	 */
	void closeAccount(Long accountId);
	
}
