/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.rpc.util.MarshallerClassRegistry;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RankListBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordListBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.mock.MockDataUtils;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.AuditDetailVO;
import com.wxxr.stock.trading.ejb.api.DealDetailVO;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.HomePageVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.StockTradingOrderVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;
import com.wxxr.stock.trading.ejb.api.TradingRecordVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;

/**
 * 交易管理模块
 * 
 * @author wangxuyang
 * 
 */
public class TradingManagementServiceImpl extends
		AbstractModule<IStockAppContext> implements ITradingManagementService {

	private static final Trace log = Trace
			.register(TradingManagementServiceImpl.class);
	// =========================beans =======================
	/**
	 * 排行榜列表
	 */
	private RankListBean rank = new RankListBean();
	/**
	 * 我的交易盘列表
	 */
	private TradingAccountListBean myTradingAccounts = new TradingAccountListBean();
	/**
	 * 我的交易盘详情
	 */
	private TradingAccountBean myTradingAccount = new TradingAccountBean();
	/**
	 * 成交详情
	 */
	private DealDetailBean dealDetailBean = new DealDetailBean();
	/**
	 * 清算详情
	 */
	private AuditDetailBean auditDetailBean = new AuditDetailBean();
	/**
	 * 创建交易盘的参数配置
	 */
	private UserCreateTradAccInfoBean createTDConfig = new UserCreateTradAccInfoBean();
	/**
	 * 交易订单记录
	 */
	private TradingRecordListBean recordsBean = new TradingRecordListBean();
	// =================module life cycle methods=============================
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
	}

	@Override
	protected void startService() {
		MarshallerClassRegistry.register(TradingAccInfoVO.class);
		MarshallerClassRegistry.register(HomePageVO.class);
		MarshallerClassRegistry.register(MegagameRankVO.class);
		MarshallerClassRegistry.register(RegularTicketVO.class);
		MarshallerClassRegistry.register(WeekRankVO.class);
		MarshallerClassRegistry.register(StockTradingOrderVO.class);
		MarshallerClassRegistry.register(GainVO.class);
		context.registerService(ITradingManagementService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(ITradingManagementService.class, this);
		MarshallerClassRegistry.unregister(TradingAccInfoVO.class);
		MarshallerClassRegistry.unregister(HomePageVO.class);
		MarshallerClassRegistry.unregister(MegagameRankVO.class);
		MarshallerClassRegistry.unregister(RegularTicketVO.class);
		MarshallerClassRegistry.unregister(WeekRankVO.class);
		MarshallerClassRegistry.unregister(StockTradingOrderVO.class);
		MarshallerClassRegistry.unregister(GainVO.class);
	}

	// =================interface method =====================================
	@Override
	//首页交易盘列表
	public TradingAccountListBean getHomePageTradingAccountList()
			throws StockAppBizException {
		/*
		 * if (context.getApplication().isInDebugMode()) {
		 * myTradingAccounts.setT1TradingAccounts(MockDataUtils.mockData(1));
		 * myTradingAccounts.setT0TradingAccounts(MockDataUtils.mockData(0));
		 * return myTradingAccounts; }
		 */
		Future<List<TradingAccInfoVO>> future = context.getExecutor().submit(
				new Callable<List<TradingAccInfoVO>>() {
					@Override
					public List<TradingAccInfoVO> call() throws Exception {
						try {
							List<TradingAccInfoVO> volist = getService(
									IRestProxyService.class).getRestService(
									TradingResourse.class)
									.getTradingAccountList();
							return volist;
						} catch (Throwable e) {
							log.error("fetch data error", e);
						}
						return null;
					}
				});
		List<TradingAccInfoVO> volist = null;
		try {
			volist = future.get();
			if (volist != null && volist.size() > 0) {
				List<TradingAccInfoBean> t0_list = new ArrayList<TradingAccInfoBean>();
				List<TradingAccInfoBean> t1_list = new ArrayList<TradingAccInfoBean>();
				for (TradingAccInfoVO vo : volist) {
					if (vo.getStatus() == 1) {
						t0_list.add(ConverterUtils.fromVO(vo));
					} else {
						t1_list.add(ConverterUtils.fromVO(vo));
					}
				}
				myTradingAccounts.setT0TradingAccounts(t0_list);
				myTradingAccounts.setT1TradingAccounts(t1_list);
			}
		} catch (Exception e) {
			log.warn("Error when fetching home page data", e);
		}
		return myTradingAccounts;
	}


	@Override
	//赚钱榜
	public RankListBean getEarnRank(final int start, final int limit) {
		List<HomePageVO> volist = null;
		try {
			volist = fetchDataFromServer(new Callable<List<HomePageVO>>() {
				public List<HomePageVO> call() throws Exception {
					try {
						List<HomePageVO> _volist = getService(
								IRestProxyService.class).getRestService(
								TradingResourse.class).getHomeList(start, limit);
						return _volist;
					} catch (Throwable e) {
						log.warn("Error when fetching earn rank", e);
						throw new StockAppBizException(e.getMessage());

					}
				}
			});
		} catch (Exception e) {
			log.warn("Error when fetching earn rank", e);
		}
		if (volist != null && volist.size() > 0) {
			List<EarnRankItemBean> beanList = new ArrayList<EarnRankItemBean>();
			for (HomePageVO vo : volist) {
				EarnRankItemBean bean = new EarnRankItemBean();
				bean.setAcctId(vo.getAccID());
				bean.setImgUrl(vo.getUrl());
				bean.setTitle(vo.getWordage());
				beanList.add(bean);
			}
			rank.setEarnRankBeans(beanList);
		}
		return rank;
	}

	public RankListBean getTMegagameRank() throws StockAppBizException {
		/*if (context.getApplication().isInDebugMode()) {
			rank.setTRankBeans(MockDataUtils.mockRankData("T"));
			return rank;
		}*/
		try {
			List<MegagameRankVO> volist = fetchDataFromServer(new Callable<List<MegagameRankVO>>() {
				public List<MegagameRankVO> call() throws Exception {
					try {
						List<MegagameRankVO> volist = getService(
								IRestProxyService.class).getRestService(
								TradingResourse.class).getTMegagameRank();
						return volist;
					} catch (Throwable e) {
						log.warn("Error when fetching T+1 rank from server", e);
						throw new StockAppBizException(e.getMessage());
					}
				}
			});
			if (volist != null && volist.size() > 0) {
				List<MegagameRankBean> beanList = new ArrayList<MegagameRankBean>();
				int rankNo = 1;
				for (MegagameRankVO vo : volist) {
					MegagameRankBean bean = ConverterUtils.fromVO(vo);
					bean.setRankSeq(rankNo++);
					beanList.add(bean);
				}
				rank.setTRankBeans(beanList);
			}
		} catch (Exception e) {
			log.warn("Error when fetching T+1 rank", e);
		}
		return rank;
	}

	@Override
	public RankListBean getT1MegagameRank() throws StockAppBizException {
		/*if (context.getApplication().isInDebugMode()) {
			rank.setT1RankBeans(MockDataUtils.mockRankData("T1"));
			return rank;
		}*/
		try {
			List<MegagameRankVO> volist = fetchDataFromServer(new Callable<List<MegagameRankVO>>() {
				public List<MegagameRankVO> call() throws Exception {
					try {
						List<MegagameRankVO> volist = getService(
								IRestProxyService.class).getRestService(
								TradingResourse.class).getTPlusMegagameRank();
						return volist;
					} catch (Throwable e) {
						log.warn("Error when fetching T+1 rank from server", e);
						throw new StockAppBizException(e.getMessage());
					}
				}
			});
			if (volist != null && volist.size() > 0) {
				List<MegagameRankBean> beanList = new ArrayList<MegagameRankBean>();
				int rankNo = 1;
				for (MegagameRankVO vo : volist) {
					MegagameRankBean bean = ConverterUtils.fromVO(vo);
					bean.setRankSeq(rankNo++);
					beanList.add(bean);
				}
				rank.setT1RankBeans(beanList);
			}
		} catch (Exception e) {
			log.warn("Error when fetching T+1 rank", e);
		}
		return rank;
	}

	@Override
	public RankListBean getRegularTicketRank() throws StockAppBizException {
	/*	if (context.getApplication().isInDebugMode()) {
			rank.setRegularTicketBeans(MockDataUtils.mockRegularTicketRank());
			return rank;
		}*/
		try {
			List<RegularTicketVO> volist = fetchDataFromServer(new Callable<List<RegularTicketVO>>() {
				public List<RegularTicketVO> call() throws Exception {
					try {
						List<RegularTicketVO> volist = getService(
								IRestProxyService.class).getRestService(
								TradingResourse.class).getRegularTicketRank();
						return volist;
					} catch (Throwable e) {
						log.warn("Error when fetching rg rank from server", e);
						throw new StockAppBizException(e.getMessage());
					}
				}
			});
			if (volist != null && volist.size() > 0) {
				List<RegularTicketBean> beanList = new ArrayList<RegularTicketBean>();
				int rankNo = 1;
				for (RegularTicketVO vo : volist) {
					RegularTicketBean bean = ConverterUtils.fromVO(vo);
					bean.setRankSeq(rankNo++);
					beanList.add(bean);
				}
				rank.setRegularTicketBeans(beanList);
			}
		} catch (Exception e) {
			log.warn("Error when fetching rg rank", e);
		}
		return rank;
	}

	@Override
	public RankListBean getWeekRank() throws StockAppBizException {
		/*if (context.getApplication().isInDebugMode()) {
			rank.setWeekRanKBeans(MockDataUtils.mockWeekRank());
			return rank;
		}*/
		try {
			List<WeekRankVO> volist = fetchDataFromServer(new Callable<List<WeekRankVO>>() {
				public List<WeekRankVO> call() throws Exception {
					try {
						List<WeekRankVO> volist = getService(
								IRestProxyService.class).getRestService(
								TradingResourse.class).getWeekRank();
						return volist;
					} catch (Throwable e) {
						log.warn("Error when fetching week rank", e);
						throw new StockAppBizException(e.getMessage());
					}
				}
			});
			if (volist != null && volist.size() > 0) {
				List<WeekRankBean> beanList = new ArrayList<WeekRankBean>();
				int rankNo = 1;
				for (WeekRankVO weekRankVO : volist) {
					WeekRankBean bean = ConverterUtils
							.fromVO(weekRankVO);
					bean.setRankSeq(rankNo++);
					beanList.add(bean);
				}
				rank.setWeekRanKBeans(beanList);
			}
		} catch (Exception e) {
			log.warn("Error when fetching week rank", e);
		}
		return rank;
		
	}

	@Override
	public TradingAccountBean getTradingAccountInfo(final String acctID)
			throws StockAppBizException {
		if (log.isDebugEnabled()) {
			log.debug("Get trading account info for id:" + acctID);
		}
		TradingAccountVO vo  = null;
		try {
			vo = fetchDataFromServer(new Callable<TradingAccountVO>() {
				public TradingAccountVO call() throws Exception {
					try {
						TradingAccountVO _vo = getService(IRestProxyService.class)
								.getRestService(TradingResourse.class).getAccount(
										acctID);
						if (log.isDebugEnabled()) {
							log.debug("fetch data:" + _vo);
						}
						return _vo;
					} catch (Exception e) {
						log.warn(String.format(
								"Error when fetch trading account info[id=%s]",
								acctID), e);
						throw new StockAppBizException("网络不给力，请稍候再试");
					}
				}
			});
		} catch (Exception e) {
			log.warn(String.format(
					"Error when fetch trading account info[id=%s]",
					acctID), e);
		}
		if (vo != null) {
			myTradingAccount.setId(vo.getId());
			myTradingAccount.setApplyFee(vo.getApplyFee());
			myTradingAccount.setAvalibleFee(vo.getAvalibleFee());
			myTradingAccount.setBuyDay(vo.getBuyDay());
			myTradingAccount.setFrozenVol(vo.getFrozenVol());
			myTradingAccount.setGainRate(vo.getGainRate());
			myTradingAccount.setLossLimit(vo.getLossLimit());
			myTradingAccount.setMaxStockCode(vo.getMaxStockCode());
			myTradingAccount.setMaxStockMarket(vo
					.getMaxStockMarket());
			myTradingAccount.setOver(vo.getOver());
			myTradingAccount.setSellDay(vo.getSellDay());
			myTradingAccount.setStatus(vo.getStatus());
			myTradingAccount.setTotalGain(vo.getTotalGain());
			myTradingAccount.setType(vo.getType());
			List<StockTradingOrderVO> orderVos = vo
					.getTradingOrders();
			if (orderVos != null) {
				List<StockTradingOrderBean> list = new ArrayList<StockTradingOrderBean>();
				for (StockTradingOrderVO order : orderVos) {
					list.add(ConverterUtils.fromVO(order));
				}
				myTradingAccount.setTradingOrders(list);
			}
			myTradingAccount.setUsedFee(vo.getUsedFee());
			myTradingAccount.setVirtual(vo.isVirtual());
		}
		
		if (log.isDebugEnabled()) {
			log.debug(myTradingAccount.toString());
		}
		return myTradingAccount;
	}
	
	public UserCreateTradAccInfoBean getUserCreateTradAccInfo() {
		try {
			UserCreateTradAccInfoVO vo = fetchDataFromServer(new Callable<UserCreateTradAccInfoVO>() {
				public UserCreateTradAccInfoVO call() throws Exception {
					UserCreateTradAccInfoVO _vo = null;
					try {
						_vo = context.getService(IRestProxyService.class)
								.getRestService(TradingResourse.class)
								.getCreateStrategyInfo();
						if (log.isDebugEnabled()) {
							log.debug("fetch data:" + _vo);
						}
					} catch (Throwable e) {
						log.warn("Failed to create trading account", e);
						throw new StockAppBizException(e.getMessage());
					}
					return _vo;
				}
			});
			if (vo != null) {
				createTDConfig.setCapitalRate(vo.getCapitalRate());
				createTDConfig.setCostRate(vo.getCostRate());
				createTDConfig.setDepositRate(vo.getDepositRate());
				createTDConfig.setMaxAmount(vo.getMaxAmount());
				createTDConfig.setRateString(vo.getRateString());
				createTDConfig.setVoucherCostRate(vo.getVoucherCostRate());
			}
		} catch (Exception e) {
			log.warn("Error when fetching create trading account config", e);
			throw new StockAppBizException("网络不给力，请稍后再试");
		}
		return createTDConfig;
	}
	
	@Override
	public DealDetailBean getDealDetail(final String acctID) {
		/*if (context.getApplication().isInDebugMode()) {
			dealDetailBean.setFund("10万");
			dealDetailBean.setPlRisk(0.001f);
			dealDetailBean.setUserGain(1000);
			dealDetailBean.setImgUrl(new String[] { "#" });
			dealDetailBean.setTradingRecords(MockDataUtils.mockTradingRecord());
			return dealDetailBean;
		}*/
		try {
			DealDetailVO vo = fetchDataFromServer(new Callable<DealDetailVO>() {
				public DealDetailVO call() throws Exception {
					try {
						DealDetailVO vo = getRestService(TradingResourse.class)
								.getDealDetail(acctID);
						if (log.isDebugEnabled()) {
							log.debug("fetch data:" + vo);
						}
						return vo;
					} catch (Throwable e) {
						log.warn("Failed to fetch deal detail", e);
						throw new StockAppBizException(e);
					}
				}
			});
			if (vo != null) {
				dealDetailBean.setFund(vo.getFund());

				dealDetailBean.setPlRisk(Float.valueOf(vo.getPlRisk()));
				dealDetailBean.setUserGain(Float.valueOf(vo
						.getUserGain()));
				dealDetailBean.setImgUrl(vo.getImgUrl());
				List<TradingRecordVO> volist = vo.getTradingRecords();
				if (volist != null && volist.size() > 0) {
					List<TradingRecordBean> beans = new ArrayList<TradingRecordBean>();
					for (TradingRecordVO tradingRecordVO : volist) {
						beans.add(ConverterUtils
								.fromVO(tradingRecordVO));
					}
					dealDetailBean.setTradingRecords(beans);
				}
			}
		} catch (Exception e) {
			log.warn("Error when fetch deal detail", e);
		}
		return dealDetailBean;
	}
	
	public AuditDetailBean getAuditDetail(final String acctId) {
		/*if (context.getApplication().isInDebugMode()) {
			log.info("getAuditDetail: accid= " + acctId);
			auditDetailBean.setAccountPay("122.3");
			auditDetailBean.setBuyDay("2013-11-12");
			auditDetailBean.setDeadline("2013-11-13");
			auditDetailBean.setBuyAverage("39.9");
			auditDetailBean.setId("123");
			auditDetailBean.setFrozenAmount("0");
			auditDetailBean.setFund("10万");
			return auditDetailBean;
		}*/
		AuditDetailVO vo = null;
		try {
			vo = fetchDataFromServer(new Callable<AuditDetailVO>() {
				public AuditDetailVO call() throws Exception {
					AuditDetailVO vo = null;
					try {
						vo = getRestService(TradingResourse.class).getAuditDetail(
								acctId);
						return vo;
					} catch (Throwable e) {
						log.warn("Failed to fetch audit detail info", e);
						throw new StockAppBizException(e.getMessage());
					}
				}
			});
		} catch (Exception e) {
			log.warn("Failed to fetch audit detail info", e);
		}
		if (vo != null) {
			auditDetailBean.setAccountPay(vo.getAccountPay());
			auditDetailBean.setBuyDay(vo.getBuyDay());
			auditDetailBean.setBuyAverage(vo.getBuyAverage());
			auditDetailBean.setCapitalRate(vo.getCapitalRate());
			auditDetailBean.setCost(vo.getCost());
			auditDetailBean.setDeadline(vo.getDeadline());
			auditDetailBean.setFrozenAmount(vo.getFrozenAmount());
			auditDetailBean.setFund(vo.getFund());
			auditDetailBean.setId(vo.getId());
			auditDetailBean.setPayOut(vo.getPayOut());
			auditDetailBean.setPlRisk(vo.getPlRisk());
			auditDetailBean.setSellAverage(vo.getSellAverage());
			auditDetailBean.setTotalGain(vo.getTotalGain());
			auditDetailBean.setTradingCost(vo.getTradingCost());
			auditDetailBean.setTradingDate(vo.getTradingDate());
			auditDetailBean.setType(vo.getType());
			auditDetailBean.setUnfreezeAmount(vo.getFrozenAmount());
			auditDetailBean.setUserGain(vo.getUserGain());
			auditDetailBean.setVirtual(vo.isVirtual());

		}
		return auditDetailBean;
	}
	
	@Override
	public void createTradingAccount(final Long captitalAmount,
			final float capitalRate, final boolean virtual,
			final float depositRate) throws StockAppBizException {
		context.invokeLater(new Runnable() {
			public void run() {
				try {
					StockResultVO vo = getRestService(TradingResourse.class)
							.createTradingAccount(captitalAmount, capitalRate,
									virtual, depositRate);
					if (vo != null) {
						if (vo.getSuccOrNot() == 0) {
							if (log.isDebugEnabled()) {
								log.debug("Failed to create trading account, caused by "
										+ vo.getCause());
							}
							throw new StockAppBizException(vo.getCause());
						}
						if (vo.getSuccOrNot() == 1) {
							if (log.isDebugEnabled()) {
								log.debug("Create trading account successfully.");
							}
						}
					}
				} catch (Throwable e) {
					log.warn("Failed to create trading account", e);
					throw new StockAppBizException(e.getMessage());
				}

			}
		}, 1, TimeUnit.SECONDS);

	}
	@Override
	public void buyStock(final String acctID, final String market,
			final String code, String price, String amount)
			throws StockAppBizException {
		final long cPrice = Long.valueOf(price);// 需要判断处理
		final long c_amount = Long.valueOf(amount);
		context.invokeLater(new Runnable() {
			public void run() {
				StockResultVO vo = null;
				try {
					vo = getRestService(TradingResourse.class).buyStock(acctID,
							market, code, cPrice, c_amount);
					if (vo != null) {
						if (vo.getSuccOrNot() == 0) {// 表示失败
							if (log.isDebugEnabled()) {
								log.debug("Failed to buy stock, caused by "
										+ vo.getCause());
							}
							throw new StockAppBizException(vo.getCause());
						}
						if (vo.getSuccOrNot() == 1) {// 表示成功
							if (log.isDebugEnabled()) {
								log.debug("Buy stock successfully.");
							}
						}
					}
				} catch (Exception e) {
					log.warn("Failed to buy stock", e);
					throw new StockAppBizException(e.getMessage());
				}

			}
		}, 1, TimeUnit.SECONDS);

	}
	@Override
	public void sellStock(final String acctID, final String market,
			final String code, String price, String amount)
			throws StockAppBizException {
		final long cPrice = Long.valueOf(price);// 需要判断处理
		final long c_amount = Long.valueOf(amount);
		context.invokeLater(new Runnable() {
			public void run() {
				StockResultVO vo = null;
				try {
					vo = getRestService(TradingResourse.class).sellStock(
							acctID, market, code, cPrice, c_amount);
					if (vo != null) {
						if (vo.getSuccOrNot() == 0) {// 表示失败
							if (log.isDebugEnabled()) {
								log.debug("Failed to sell stock, caused by "
										+ vo.getCause());
							}
							throw new StockAppBizException(vo.getCause());
						}
						if (vo.getSuccOrNot() == 1) {// 表示成功
							if (log.isDebugEnabled()) {
								log.debug("Sell stock successfully.");
							}
						}
					}
				} catch (Exception e) {
					log.warn("Failed to sell stock", e);
					throw new StockAppBizException(e.getMessage());
				}

			}
		}, 1, TimeUnit.SECONDS);
	}
	@Override
	public void cancelOrder(final String orderID) throws StockAppBizException {
		context.invokeLater(new Runnable() {
			public void run() {
				StockResultVO vo = null;
				try {
					vo = getRestService(TradingResourse.class).cancelOrder(
							orderID);
					if (vo != null) {
						if (vo.getSuccOrNot() == 0) {// 表示失败
							if (log.isDebugEnabled()) {
								log.debug("Failed to cancel order, caused by "
										+ vo.getCause());
							}
							throw new StockAppBizException(vo.getCause());
						}
						if (vo.getSuccOrNot() == 1) {// 表示成功
							if (log.isDebugEnabled()) {
								log.debug("Cancel order successfully.");
							}
						}
					}
				} catch (Exception e) {
					log.warn("Failed to cancel order", e);
					throw new StockAppBizException(e.getMessage());
				}

			}
		}, 1, TimeUnit.SECONDS);
	}
	@Override
	public void quickBuy(final Long captitalAmount, String capitalRate,
			final boolean virtual, final String stockMarket,
			final String stockCode, String stockBuyAmount, String depositRate)
			throws StockAppBizException {

		final float _capitalRate = Float.valueOf(capitalRate);
		final long _stockBuyAmount = Long.valueOf(stockBuyAmount);
		final float _depositRate = Float.valueOf(depositRate);
		context.invokeLater(new Runnable() {
			public void run() {
				StockResultVO vo = null;
				try {
					vo = getRestService(TradingResourse.class).quickBuy(
							captitalAmount, _capitalRate, virtual, stockMarket,
							stockCode, _stockBuyAmount, _depositRate);
					if (vo != null) {
						if (vo.getSuccOrNot() == 0) {// 表示失败
							if (log.isDebugEnabled()) {
								log.debug("Failed to cancel order, caused by "
										+ vo.getCause());
							}
							throw new StockAppBizException(vo.getCause());
						}
						if (vo.getSuccOrNot() == 1) {// 表示成功
							if (log.isDebugEnabled()) {
								log.debug("Cancel order successfully.");
							}
						}
					}
				} catch (Throwable e) {
					log.warn("Failed to cancel order", e);
					throw new StockAppBizException(e.getMessage());
				}

			}
		}, 1, TimeUnit.SECONDS);

	}
	@Override
	public void clearTradingAccount(final String acctID) {
		context.invokeLater(new Runnable() {
			public void run() {
				StockResultVO vo = null;
				try {
					vo = getRestService(TradingResourse.class)
							.clearTradingAccount(acctID);
					if (vo != null) {
						if (vo.getSuccOrNot() == 0) {// 表示失败
							if (log.isDebugEnabled()) {
								log.debug("Failed to clear trading account, caused by "
										+ vo.getCause());
							}
							throw new StockAppBizException(vo.getCause());
						}
						if (vo.getSuccOrNot() == 1) {// 表示成功
							if (log.isDebugEnabled()) {
								log.debug("Clear trading account successfully.");
							}
						}
					}
				} catch (Throwable e) {
					log.warn("Failed to cancel order", e);
					throw new StockAppBizException(e.getMessage());
				}

			}
		}, 1, TimeUnit.SECONDS);

	}
	@Override
	public TradingAccountListBean getMyAllTradingAccountList(final int start,
			final int limit) {
		if (context.getApplication().isInDebugMode()) {
			myTradingAccounts.setAllTradingAccounts(MockDataUtils.mockTradeRecord(1));
			return myTradingAccounts;
		}
		List<GainVO> volist = null;
		try {
			volist = fetchDataFromServer(new Callable<List<GainVO>>() {
				public List<GainVO> call() throws Exception {
					try {
						if (log.isDebugEnabled()) {
							log.debug("fetch all trading account info...");
						}
						List<GainVO> list = getRestService(TradingResourse.class)
								.getTotalGain(start, limit);
						return list;
					} catch (Throwable e) {
						log.warn("Failed to fetch all trading account", e);
						throw new StockAppBizException(e.getMessage());
					}
				}
			});
		} catch (Exception e) {
			log.warn("Failed to fetch all trading account", e);
		}
		if (volist != null && volist.size() > 0) {
			List<GainBean> beanList = new ArrayList<GainBean>();
			for (GainVO vo : volist) {
				GainBean bean = ConverterUtils.fromVO(vo);
				beanList.add(bean);
			}
			myTradingAccounts.setAllTradingAccounts(beanList);
		}
		return myTradingAccounts;
	}

	@Override
	public TradingAccountListBean getMySuccessTradingAccountList(
			final int start, final int limit) {
		if (context.getApplication().isInDebugMode()) {
			myTradingAccounts.setSuccessTradingAccounts(MockDataUtils.mockTradeRecord(0));
			return myTradingAccounts;
		}
		List<GainVO> volist = null;
		try {
			volist = fetchDataFromServer(new Callable<List<GainVO>>() {
				public List<GainVO> call() throws Exception {
					try {
						if (log.isDebugEnabled()) {
							log.debug("fetch all trading account info...");
						}
						List<GainVO> list = getRestService(TradingResourse.class)
								.getGain(start, limit);
						return list;
					} catch (Throwable e) {
						log.warn("Failed to fetch success trading account", e);
						throw new StockAppBizException(e.getMessage());
					}
				}
			});
		} catch (Exception e) {
			log.warn("Failed to fetch success trading account", e);
		}
		if (volist != null && volist.size() > 0) {
			List<GainBean> beanList = new ArrayList<GainBean>();
			for (GainVO vo : volist) {
				GainBean bean = ConverterUtils.fromVO(vo);
				beanList.add(bean);
			}
			myTradingAccounts.setSuccessTradingAccounts(beanList);
		}
		return myTradingAccounts;
	}
	public TradingRecordListBean getTradingAccountRecord(final String acctID,
			final int start, final int limit) {
		List<TradingRecordVO> volist = null;
		try {
			volist = fetchDataFromServer(new Callable<List<TradingRecordVO>>() {
				public List<TradingRecordVO> call() throws Exception {
					try {
						List<TradingRecordVO> volist = getRestService(
								TradingResourse.class).getTradingAccountRecord(
								acctID, start, limit);
						return volist;
					} catch (Exception e) {
						log.warn("Error when getting trading record", e);
						throw new StockAppBizException(e.getMessage());
					}
				}
			});
		} catch (Exception e) {
			log.warn("Error when getting trading record", e);
		}
		if (volist != null && volist.size() > 0) {
			List<TradingRecordBean> beans = new ArrayList<TradingRecordBean>();
			for (TradingRecordVO vo : volist) {
				TradingRecordBean bean = ConverterUtils.fromVO(vo);
				beans.add(bean);
			}
			recordsBean.setRecords(beans);
		}
		return recordsBean;
	}
	// =================private method =======================================
	private <T> T fetchDataFromServer(Callable<T> task) throws Exception{
		Future<T> future = context.getExecutor().submit(task);
		T result = null;
		try {
			result = future.get();
			return result;
		} catch (Exception e) {
			log.warn("Error when fetching data from server", e);
			throw e;
		}
	}

	private <T> T getRestService(Class<T> restResouce) {
		return context.getService(IRestProxyService.class).getRestService(
				restResouce);
	}
	

}
