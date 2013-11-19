/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.RankListBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordListBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;

/**
 * 交易管理模块
 * 
 * @author wangxuyang
 * 
 */
public interface ITradingManagementService {
	/**
	 * 获取当前用户全部交易盘记录
	 * 
	 * @return
	 */
	TradingAccountListBean getMyAllTradingAccountList(int strat,int limit);
	/**
	 * 获取当前用户成功操作交易盘记录
	 * 
	 * @return
	 */
	TradingAccountListBean getMySuccessTradingAccountList(int strat,int limit);
	/**
	 * 获取当前用户首页交易盘列表
	 * 
	 * @return
	 * @throws StockAppBizException
	 */
	TradingAccountListBean getHomePageTradingAccountList() throws StockAppBizException;
	/***
	 * 
	 * @param acctID - 交易盘Id
	 * @return
	 * @throws StockAppBizException
	 */
	TradingAccountBean getTradingAccountInfo(String acctID) throws StockAppBizException;
	
	//======================交易部分接口 =======================
	
	/**
	 * 创建交易盘
	 * 
	 * @param captitalAmount
	 *            -申请额度
	 * @param capitalRate
	 *            -中止止损
	 * @param virtual
	 *            - 是否为虚拟盘 - true表示虚拟盘；false表示实盘
	 * @param depositRate
	 *            - 保证金
	 * @throws StockAppBizException
	 *             业务异常（网络异常）
	 */
	void createTradingAccount(Long captitalAmount, float capitalRate,
			boolean virtual, float depositRate) throws StockAppBizException;

	/**
	 * 买入股票
	 * 
	 * @param acctID
	 *            -交易盘ID
	 * @param market
	 *            -市场代码： SH，SZ各代表上海，深圳
	 * @param code
	 *            - 股票代码
	 * @param price
	 *            - 委托价
	 * @param amount
	 *            - 委托数量
	 * @throws StockAppBizException
	 */
	void buyStock(String acctID, String market, String code, String price,
			String amount) throws StockAppBizException;
	/**
	 * 卖出股票
	 * @param acctID -交易盘ID
	 * @param market -市场代码： SH，SZ各代表上海，深圳
	 * @param code -股票代码
	 * @param price -委托价
	 * @param amount -委托数量
	 * @throws StockAppBizException
	 */
	void sellStock(String acctID,String market,String code,String price,String amount) throws StockAppBizException;

	/**
	 * 撤单
	 * @param orderID -交易订单ID
	 */
	void cancelOrder(String orderID);
	/**
	 * 清算交易盘
	 * @param acctID - 交易盘Id
	 */
	public void clearTradingAccount(String acctID);
	/**
	 * 快速买入
	  * @param captitalAmount
	 *            -申请额度
	 * @param capitalRate
	 *            -中止止损
	 * @param virtual
	 *            - 是否为虚拟盘 - true表示虚拟盘；false表示实盘
	 * @param depositRate - 保证金
	 * @param stockMarket - 市场代码： SH，SZ各代表上海，深圳
	 * @param stockCode -股票代码
	 * @param stockBuyAmount -委托价
	 * @param depositRate -委托数量
	 * @throws StockAppBizException
	 */
	void quickBuy(Long captitalAmount,String capitalRate,boolean virtual,String stockMarket,String stockCode,String
			stockBuyAmount,String depositRate)throws StockAppBizException;
	//======================交易部分接口  end =======================
	/**
	 * 获取他人交易盘列表
	 * 
	 * @param userId
	 * @param type
	 *            - 0表示T日交易盘，1表示T+1日交易盘
	 * @return
	 */
	TradingAccountListBean getOtherTradingAccountList(String userId);
	/**
	 * 获取成交详情
	 * 
	 * @param accId
	 *            -交易盘Id
	 * @return
	 */
	DealDetailBean getDealDetail(String accId);

	/**
	 * 获取清算详情
	 * 
	 * @param accId
	 *            -交易盘Id
	 * @return
	 */
	AuditDetailBean getAuditDetail(String accId);
	//========================排行榜 ============================
	/**
	 * 获取赚钱榜
	 * @param start
	 * @param limit
	 * @return
	 */
	public RankListBean getEarnRank(int start,int limit);
	/**
	 * 获取T日排行榜
	 * 
	 * @return
	 * @throws StockAppBizException
	 */
	RankListBean getTMegagameRank() throws StockAppBizException;

	/**
	 * 获取T+1日排行榜
	 * 
	 * @return
	 * @throws StockAppBizException
	 */
	RankListBean getT1MegagameRank() throws StockAppBizException;

	/**
	 * 获取实盘券排行榜
	 * 
	 * @return
	 * @throws StockAppBizException
	 */
	RankListBean getRegularTicketRank() throws StockAppBizException;

	/**
	 * 获取周赛排行榜
	 * 
	 * @return
	 * @throws StockAppBizException
	 */
	RankListBean getWeekRank() throws StockAppBizException;

	/**
	 * 获取创建交易盘相关参数
	 * 
	 * @return
	 */
	UserCreateTradAccInfoBean getUserCreateTradAccInfo();
	
	
	/**
	 * 交易记录
	 * @param acctID
	 * @param start
	 * @param limit
	 * @return
	 */
	TradingRecordListBean getTradingAccountRecord(String acctID,int start,int limit);
}
