/**
 * 
 */
package com.wxxr.mobile.stock.client.service;

import java.util.List;

import com.wxxr.mobile.stock.client.StockAppBizException;
import com.wxxr.mobile.stock.client.bean.TradingAccount;

/**
 * 交易管理模块
 * @author wangxuyang
 *
 */
public interface ITradingManagementService{
	/**
	 * 获取当前用户交易盘列表
	 * @param type- 0表示T日交易盘，1表示T+1日交易盘
	 * @return
	 * @throws StockAppBizException
	 */
	List<TradingAccount> getTradingAccountList(int type) throws StockAppBizException;
	/**
	 * 获取他人交易盘列表
	 * @param userId 
	 * @param type - 0表示T日交易盘，1表示T+1日交易盘
	 * @return
	 */
	List<TradingAccount> getTradingAccountList(String userId,int type);
	/**
	 * 
	 * 获取交易盘
	 * @param id - 交易盘ID
	 * @return
	 */
	TradingAccount getTradingAccount(Long id);
	/**
	 * 创建交易盘
	 * @param type-交易盘类型
	 * 			 0表示模拟盘；1表示余额实盘，2表示积分实盘
	 * @param credit - 申请额度
	 * @param stops - 止损
	 * @param fee1 - 交易综合费
	 * @param fee2 - 冻结余额
	 * @return
	 * @throws StockAppBizException 业务异常（用户未登录、网络异常等）
	 */
	Long createTradingAccount(String type,String credit,String stops,String fee1,String freezing) throws StockAppBizException;

}
