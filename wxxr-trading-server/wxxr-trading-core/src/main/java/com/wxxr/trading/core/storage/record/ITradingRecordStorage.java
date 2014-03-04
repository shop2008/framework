/**
 * 
 */
package com.wxxr.trading.core.storage.record;

import com.wxxr.trading.core.storage.api.IBizObjectStorage;
import com.wxxr.trading.core.storage.record.persistence.bean.TradingRecordInfo;

/**
 * @author neillin
 *
 */
public interface ITradingRecordStorage extends IBizObjectStorage<Long, TradingRecordObject, TradingRecordInfo> {

}
