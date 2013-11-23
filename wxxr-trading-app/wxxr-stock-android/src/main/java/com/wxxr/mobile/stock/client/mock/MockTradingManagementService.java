/**
 * 
 */
package com.wxxr.mobile.stock.client.mock;

import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordListBean;
import com.wxxr.mobile.stock.app.mock.MockDataUtils;
import com.wxxr.mobile.stock.app.service.impl.TradingManagementServiceImpl;

/**
 * @author wangxuyang
 * 
 */
public class MockTradingManagementService extends TradingManagementServiceImpl {
	@Override
	public TradingAccountBean getTradingAccountInfo(final String acctID)
			throws StockAppBizException {
		myTradingAccount = MockDataUtils.mockTradingAccountInfo();
		return myTradingAccount;
	}

	@Override
	public TradingRecordListBean getTradingAccountRecord(final String acctID,
			final int start, final int limit) {
		recordsBean.setRecords(MockDataUtils.mockTradingRecord());
		return recordsBean;
	}
}
