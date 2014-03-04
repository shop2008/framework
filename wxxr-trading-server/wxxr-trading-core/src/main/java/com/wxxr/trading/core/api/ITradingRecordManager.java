/**
 * 
 */
package com.wxxr.trading.core.api;

import java.util.List;

import com.wxxr.trading.core.model.IAssetAmount;
import com.wxxr.trading.core.model.IAssetTradingRecord;
import com.wxxr.trading.core.model.ITradingRecord;
import com.wxxr.trading.core.model.TradingType;

/**
 * @author neillin
 *
 */
public interface ITradingRecordManager {
	/**
	 * ����µĽ��׼�¼
	 * @param accountId �ʲ��˻�Id
	 * @param type		��������
	 * @param tradingAmount	���׽��
	 * @param comments	��ע
	 * @param params	��������
	 */
	ITradingRecord addRecord(Long accountId, TradingType type, IAssetAmount tradingAmount,String comments,Object... params);
	
	/**
	 * ����ָ���˻������н��׼�¼
	 * @param accountId
	 * @return
	 */
	List<ITradingRecord> findRecords(Long accountId);
	/**
	 *  ����ָ���˻��ķ�ҳ������н��׼�¼
	 * @param accountId
	 * @param start
	 * @param limit
	 * @return
	 */
	List<IAssetTradingRecord> findSubRecords(Long assetId,int start,int limit);
	
	/**
	 * ����ָ��id�Ľ��׼�¼
	 * @param recdId
	 * @return
	 */
	ITradingRecord getRecord(Long recdId);
	/**
	 * �����û���IDȡ���û�ʵ��ȯ�ۼ�����
	 * @param acctId
	 * @return
	 */
	Long findAccumulateVoucherByRecords(Long userId);
	/**
	 * @param assetId �ʲ�ID
	 * @param date yyyyMMdd
	 * @return ǩ���ʲ���¼
	 */
	IAssetTradingRecord findSignRecordByUser(Long assetId,String date);
}
