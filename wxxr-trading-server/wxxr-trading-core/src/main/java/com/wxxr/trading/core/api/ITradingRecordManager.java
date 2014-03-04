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
	 * 添加新的交易记录
	 * @param accountId 资产账户Id
	 * @param type		交易类型
	 * @param tradingAmount	交易金额
	 * @param comments	备注
	 * @param params	其他参数
	 */
	ITradingRecord addRecord(Long accountId, TradingType type, IAssetAmount tradingAmount,String comments,Object... params);
	
	/**
	 * 查找指定账户的所有交易记录
	 * @param accountId
	 * @return
	 */
	List<ITradingRecord> findRecords(Long accountId);
	/**
	 *  查找指定账户的分页后的所有交易记录
	 * @param accountId
	 * @param start
	 * @param limit
	 * @return
	 */
	List<IAssetTradingRecord> findSubRecords(Long assetId,int start,int limit);
	
	/**
	 * 返回指定id的交易记录
	 * @param recdId
	 * @return
	 */
	ITradingRecord getRecord(Long recdId);
	/**
	 * 根据用户的ID取得用户实盘券累计数量
	 * @param acctId
	 * @return
	 */
	Long findAccumulateVoucherByRecords(Long userId);
	/**
	 * @param assetId 资产ID
	 * @param date yyyyMMdd
	 * @return 签到资产记录
	 */
	IAssetTradingRecord findSignRecordByUser(Long assetId,String date);
}
