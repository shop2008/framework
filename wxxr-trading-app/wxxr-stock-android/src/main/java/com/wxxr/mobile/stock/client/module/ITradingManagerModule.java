/**
 * 
 */
package com.wxxr.mobile.stock.client.module;

import java.util.List;

import com.wxxr.mobile.stock.client.StockAppBizException;
import com.wxxr.mobile.stock.client.bean.TradingAccount;

/**
 * @author wangxuyang
 *
 */
public interface ITradingManagerModule{
	/**
	 * 获取当前用户交易盘列表
	 * @param type- 0表示T日交易盘，1表示T+1日交易盘
	 * @return
	 */
	List<TradingAccount> getTradingAccountList(int type) throws StockAppBizException;
	/**
	 * 获取他人交易盘列表
	 * @param userId 
	 * @param type - 0表示T日交易盘，1表示T+1日交易盘
	 * @return
	 */
	List<TradingAccount> getTradingAccountList(String userId,int type);

}
