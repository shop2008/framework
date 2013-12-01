/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.IAsyncCallback;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordListBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.mock.MockDataUtils;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.impl.NewTradingManagementServiceImpl.TradingAccInfoBeanComparator;
import com.wxxr.mobile.stock.app.service.loader.EarnRankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.RegularTicketRankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.RightGainLoader;
import com.wxxr.mobile.stock.app.service.loader.T1RankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.TRankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.TradingAccountInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.UserCreateTradAccInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.WeekRankItemLoader;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.mobile.stock.trade.command.BuyStockCommand;
import com.wxxr.mobile.stock.trade.command.BuyStockHandler;
import com.wxxr.mobile.stock.trade.command.CreateTradingAccountCommand;
import com.wxxr.mobile.stock.trade.command.CreateTradingAccountHandler;
import com.wxxr.mobile.stock.trade.command.QuickBuyStockCommand;
import com.wxxr.mobile.stock.trade.command.QuickBuyStockHandler;
import com.wxxr.mobile.stock.trade.command.SellStockCommand;
import com.wxxr.mobile.stock.trade.command.SellStockHandler;
import com.wxxr.mobile.stock.trade.entityloader.TradingAccInfoLoader;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingResource;
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

	private static final Trace log = Trace.register(TradingManagementServiceImpl.class);
	private static final Comparator<MegagameRankBean> tRankComparator = new Comparator<MegagameRankBean>() {
		
		@Override
		public int compare(MegagameRankBean o1, MegagameRankBean o2) {
			return o1.getRankSeq() - o2.getRankSeq();
		}
	};
	
	private static final Comparator<EarnRankItemBean> earnRankComparator = new Comparator<EarnRankItemBean>() {
		
		@Override
		public int compare(EarnRankItemBean o1, EarnRankItemBean o2) {
			return 0;
		}
	};
		
	private static final Comparator<WeekRankBean> weekRankComparator = new Comparator<WeekRankBean>() {
		
		@Override
		public int compare(WeekRankBean o1, WeekRankBean o2) {
			return o1.getRankSeq() - o2.getRankSeq();
		}
	};
	
	private static final Comparator<RegularTicketBean> rtRankComparator = new Comparator<RegularTicketBean>() {
		
		@Override
		public int compare(RegularTicketBean o1, RegularTicketBean o2) {
			return o1.getRankSeq() - o2.getRankSeq();
		}
	};
	// =========================beans =======================
	
	private IReloadableEntityCache<String, EarnRankItemBean> earnRankCache;
	private IReloadableEntityCache<String, MegagameRankBean> tRankCache;
	private IReloadableEntityCache<String, MegagameRankBean> t1RankCache;
	private IReloadableEntityCache<String, WeekRankBean> weekRankCache;
	private IReloadableEntityCache<String, RegularTicketBean> rtRankCache;
	/**
	 * 排行榜列表
	 */
	protected BindableListWrapper<EarnRankItemBean> earnRank;
	
	protected BindableListWrapper<MegagameRankBean> tRank;
	
	protected BindableListWrapper<MegagameRankBean> t1Rank;
	
	protected BindableListWrapper<WeekRankBean> weekRank;
	
	protected BindableListWrapper<RegularTicketBean> rtRank;

	private GenericReloadableEntityCache<String, GainBean,GainVO> rightTotalGainCache;

	/**
	 * 首页右侧交易记录-全部操作
	 */
	protected BindableListWrapper<GainBean> rightTotalGain;

	/**
	 * 首页右侧交易记录-成功操作
	 */
	protected BindableListWrapper<GainBean> rightGain;
	
	/**
	 * 我的交易盘列表
	 */
	protected TradingAccountListBean myTradingAccounts = new TradingAccountListBean();
	/**
	 * 我的交易盘详情
	 */
	protected TradingAccountBean myTradingAccount = new TradingAccountBean();
	/**
	 * 成交详情
	 */
	protected DealDetailBean dealDetailBean = new DealDetailBean();
	/**
	 * 清算详情
	 */
	protected AuditDetailBean auditDetailBean = new AuditDetailBean();
	/**
	 * 创建交易盘的参数配置
	 */
	protected UserCreateTradAccInfoBean createTDConfig = new UserCreateTradAccInfoBean();
	/**
	 * 交易订单记录
	 */
	protected TradingRecordListBean recordsBean = new TradingRecordListBean();
	
	//交易盘
    private GenericReloadableEntityCache<String,TradingAccInfoBean,List> tradingAccInfo_cache;
    //交易盘详细信息
    private GenericReloadableEntityCache<Long,TradingAccountBean,List> tradingAccountBean_cache;
    protected UserCreateTradAccInfoBean userCreateTradAccInfo;
    private IReloadableEntityCache<String, UserCreateTradAccInfoBean> userCreateTradAccInfo_Cache;

	// =================module life cycle methods=============================
	
	
	@Override
	protected void initServiceDependency() {
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
	}

	@Override
	protected void startService() {
		IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
		registry.registerEntityLoader("earnRank", new EarnRankItemLoader());
		registry.registerEntityLoader("tRank", new TRankItemLoader());
		registry.registerEntityLoader("t1Rank", new T1RankItemLoader());
		registry.registerEntityLoader("weekRank", new WeekRankItemLoader());
		registry.registerEntityLoader("rtRank", new RegularTicketRankItemLoader());
		registry.registerEntityLoader("rightTotalGain", new RightGainLoader());
		
        tradingAccInfo_cache=new GenericReloadableEntityCache<String,TradingAccInfoBean,List>("tradingAccInfo",30);
        tradingAccountBean_cache=new GenericReloadableEntityCache<Long, TradingAccountBean, List>("TradingAccountInfo");
        registry.registerEntityLoader("tradingAccInfo", new TradingAccInfoLoader());
        registry.registerEntityLoader("UserCreateTradAccInfo", new UserCreateTradAccInfoLoader());
        registry.registerEntityLoader("TradingAccountInfo", new TradingAccountInfoLoader());
        context.getService(ICommandExecutor.class).registerCommandHandler(CreateTradingAccountCommand.Name, new CreateTradingAccountHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(BuyStockCommand.Name, new BuyStockHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(SellStockCommand.Name, new SellStockHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(QuickBuyStockCommand.Name, new QuickBuyStockHandler());
        
		context.registerService(ITradingManagementService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(ITradingManagementService.class, this);
	}

	// =================interface method =====================================
	


	@Override
	//赚钱榜
	public BindableListWrapper<EarnRankItemBean> getEarnRank(final int start, final int limit) {
		if (log.isDebugEnabled()) {
			log.debug(String.format("params:[start=%s,limit=%s]", start,limit));
		}
		if(this.earnRank == null){
			if(this.earnRankCache == null){
				this.earnRankCache = new GenericReloadableEntityCache<String, EarnRankItemBean, HomePageVO>("earnRank");
			}
			this.earnRank = this.earnRankCache.getEntities(null, null);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		this.earnRankCache.doReloadIfNeccessay(params);
		this.earnRankCache.clear();
		return this.earnRank;
	}

	public synchronized BindableListWrapper<MegagameRankBean> getTMegagameRank() throws StockAppBizException {
		if(this.tRank == null){
			if(this.tRankCache == null){
				this.tRankCache = new GenericReloadableEntityCache<String, MegagameRankBean, MegagameRankVO>("tRank");
			}
			this.tRank = this.tRankCache.getEntities(null, tRankComparator);
		}
		this.tRankCache.doReloadIfNeccessay();
		this.tRank.clear();
		return this.tRank;
	}

	@Override
	public synchronized BindableListWrapper<MegagameRankBean> getT1MegagameRank() throws StockAppBizException {
		if(this.t1Rank == null){
			if(this.t1RankCache == null){
				this.t1RankCache = new GenericReloadableEntityCache<String, MegagameRankBean, MegagameRankVO>("t1Rank");
			}
			this.t1Rank = this.t1RankCache.getEntities(null, tRankComparator);
		}
		this.t1RankCache.doReloadIfNeccessay();
		this.t1Rank.clear();
		return this.t1Rank;

	}

	@Override
	public synchronized BindableListWrapper<RegularTicketBean> getRegularTicketRank() throws StockAppBizException {
		if(this.rtRank == null){
			if(this.rtRankCache == null){
				this.rtRankCache = new GenericReloadableEntityCache<String, RegularTicketBean, RegularTicketVO>("rtRank");
			}
			this.rtRank = this.rtRankCache.getEntities(null, rtRankComparator);
		}
		this.rtRankCache.doReloadIfNeccessay();
		this.rtRank.clear();
		return this.rtRank;
	}

	@Override
	public synchronized BindableListWrapper<WeekRankBean> getWeekRank() throws StockAppBizException {
		if(this.weekRank == null){
			if(this.weekRankCache == null){
				this.weekRankCache = new GenericReloadableEntityCache<String, WeekRankBean, WeekRankVO>("weekRank");
			}
			this.weekRank = this.weekRankCache.getEntities(null, weekRankComparator);
		}
		this.weekRankCache.doReloadIfNeccessay();
		this.weekRank.clear();
		return this.weekRank;

	}

	
	
	
	
	@Override
	public DealDetailBean getDealDetail(final String acctID) {
		if (context.getApplication().isInDebugMode()) {
			dealDetailBean.setFund("10万");
			dealDetailBean.setPlRisk(0.001f);
			dealDetailBean.setUserGain(1000);
			dealDetailBean.setImgUrl(new String[] { "#" });
			dealDetailBean.setTradingRecords(MockDataUtils.mockTradingRecord());
			return dealDetailBean;
		}
		try {
			DealDetailVO vo = fetchDataFromServer(new Callable<DealDetailVO>() {
				public DealDetailVO call() throws Exception {
					try {
						DealDetailVO vo = getRestService(ITradingResource.class)
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
		if (context.getApplication().isInDebugMode()) {
			log.info("getAuditDetail: accid= " + acctId);
			auditDetailBean.setAccountPay("122.3");
			auditDetailBean.setBuyDay("2013-11-12");
			auditDetailBean.setDeadline("2013-11-13");
			auditDetailBean.setBuyAverage("39.9");
			auditDetailBean.setId("123");
			auditDetailBean.setFrozenAmount("0");
			auditDetailBean.setFund("10万");
			return auditDetailBean;
		}
		AuditDetailVO vo = null;
		try {
			vo = fetchDataFromServer(new Callable<AuditDetailVO>() {
				public AuditDetailVO call() throws Exception {
					AuditDetailVO vo = null;
					try {
						vo = getRestService(ITradingResource.class).getAuditDetail(
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
	public void clearTradingAccount(final String acctID) {
		context.invokeLater(new Runnable() {
			public void run() {
				StockResultVO vo = null;
				try {
					vo = getRestService(ITradingProtectedResource.class)
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
						List<GainVO> list = getRestService(ITradingProtectedResource.class)
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
						List<GainVO> list = getRestService(ITradingProtectedResource.class)
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
			List<GainBean> vbeanList = new ArrayList<GainBean>();
			List<GainBean> rbeanList = new ArrayList<GainBean>();
			for (GainVO vo : volist) {
				GainBean bean = ConverterUtils.fromVO(vo);
				beanList.add(bean);
				if (vo.isVirtual()) {
					vbeanList.add(bean);
				}else{
					rbeanList.add(bean);
				}
			}
			myTradingAccounts.setSuccessTradingAccounts(beanList);
//			myTradingAccounts.setVirtualTradingAccounts(vbeanList);
//			myTradingAccounts.setRealTradingAccounts(rbeanList);
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
								ITradingResource.class).getTradingAccountRecord(
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

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadTMegagameRank(boolean)
	 */
	@Override
	public void reloadTMegagameRank(boolean wait4Finish) {
		tRankCache.forceReload(wait4Finish);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadT1MegagameRank(boolean)
	 */
	@Override
	public void reloadT1MegagameRank(boolean wait4Finish) {
		t1RankCache.forceReload(wait4Finish);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadRegularTicketRank(boolean)
	 */
	@Override
	public void reloadRegularTicketRank(boolean wait4Finish) {
		rtRankCache.forceReload(wait4Finish);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadWeekRank(boolean)
	 */
	@Override
	public void reloadWeekRank(boolean wait4Finish) {
		weekRankCache.forceReload(wait4Finish);
	}

	@Override
	public void reloadEarnRank(int start, int limit, boolean wait4Finish) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("limit", limit);
		this.earnRankCache.forceReload(map, wait4Finish);
	}

	

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getGain(int, int)
	 */
	@Override
	public BindableListWrapper<GainBean> getTotalGain(int start, int limit) {
		if(rightTotalGain==null){
				this.rightTotalGain = getRightTotalGainCache().getEntities(null, null);
		}
		rightTotalGainCacheDoReload(start, limit);
		return this.rightTotalGain;

	}

	protected void rightTotalGainCacheDoReload(int start, int limit) {
		synchronized (getRightTotalGainCache()) {
			Map<String,Object> commandParameters=new HashMap<String,Object>();
			commandParameters.put("start", start);
			commandParameters.put("limit", limit);
			getRightTotalGainCache().doReloadIfNeccessay(commandParameters);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getGain(int, int)
	 */
	@Override
	public BindableListWrapper<GainBean> getGain(int start, int limit) {
		if(rightGain==null){
			this.rightGain =getRightTotalGainCache().getEntities(new IEntityFilter<GainBean>() {
				
				@Override
				public boolean doFilter(GainBean entity) {
					return entity.getTotalGain()>0?true:false;
				}
			} , null);
		}
		rightTotalGainCacheDoReload(start, limit);
		return this.rightGain;
	}

	protected GenericReloadableEntityCache<String, GainBean, GainVO> getRightTotalGainCache() {
		if(rightTotalGainCache==null){
			rightTotalGainCache=new GenericReloadableEntityCache<String, GainBean, GainVO>("rightTotalGain");
		}
		return rightTotalGainCache;
	}
  
    //获取我的T日交易盘
    public BindableListWrapper<TradingAccInfoBean> getT0TradingAccountList(){
        BindableListWrapper<TradingAccInfoBean> t0s = tradingAccInfo_cache.getEntities(new IEntityFilter<TradingAccInfoBean>(){
            @Override
            public boolean doFilter(TradingAccInfoBean entity) {
                if (entity.getStatus()==1){
                    return true;
                }
                return false;
            }
            
        }, new  TradingAccInfoBeanComparator());
        return t0s;
    }
    //获取我的T+1日交易盘
    public BindableListWrapper<TradingAccInfoBean> getT1TradingAccountList(){
        BindableListWrapper<TradingAccInfoBean> t1s = tradingAccInfo_cache.getEntities(new IEntityFilter<TradingAccInfoBean>(){
            @Override
            public boolean doFilter(TradingAccInfoBean entity) {
                if (entity.getStatus()!=1){
                    return true;
                }
                return false;
            }
            
        }, new TradingAccInfoBeanComparator());
        return t1s;
    }
    //根据交易盘创建时间排序
    class TradingAccInfoBeanComparator implements Comparator<TradingAccInfoBean> {
        
        @Override
        public int compare(TradingAccInfoBean b1, TradingAccInfoBean b2) {
            if (b1!=null && b2!=null){
                return b1.getCreateDate()>b2.getCreateDate()?-1:1;
            }
            return 0;
        }
    }
    //delete
    @Override
    public TradingAccountListBean getHomePageTradingAccountList() throws StockAppBizException {
        TradingAccountListBean b=new TradingAccountListBean();
        
        return null;
    }
    
   //获取参数
    @Override
    public UserCreateTradAccInfoBean getUserCreateTradAccInfo() {
        if(this.userCreateTradAccInfo == null){
            if(this.userCreateTradAccInfo_Cache == null){
                this.userCreateTradAccInfo_Cache = new GenericReloadableEntityCache<String, UserCreateTradAccInfoBean, UserCreateTradAccInfoVO>("UserCreateTradAccInfo");
                this.userCreateTradAccInfo_Cache.putEntity("userId", new UserCreateTradAccInfoBean());
            }
            this.userCreateTradAccInfo = this.userCreateTradAccInfo_Cache.getEntity("userId");//todo key 
        }
        this.userCreateTradAccInfo_Cache.forceReload(false);
        return this.userCreateTradAccInfo;
    }

    @Override
    public void createTradingAccount(Long captitalAmount, float capitalRate, boolean virtual, float depositRate) throws StockAppBizException {
        context.getService(ICommandExecutor.class).submitCommand(new CreateTradingAccountCommand(captitalAmount,  capitalRate,  virtual,  depositRate), new IAsyncCallback() {
            @Override
            public void failed(Object cause) {
                log.error("createTradingAccount fail" + cause.toString());
            }
            @Override
            public void success(Object result) {
                if (result != null && result instanceof StockResultVO) {
                    StockResultVO vo=(StockResultVO) result;
                   
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
               
            }
        });
    }
    
    @Override
    public void buyStock(String acctID, String market, String code, String price, String amount) throws StockAppBizException {
        context.getService(ICommandExecutor.class).submitCommand(new BuyStockCommand( acctID,  market,  code,  price,  amount), new IAsyncCallback() {
            @Override
            public void failed(Object cause) {
                log.error("createTradingAccount fail" + cause.toString());
            }
            @Override
            public void success(Object result) {
                if (result != null && result instanceof StockResultVO) {
                    StockResultVO vo=(StockResultVO) result;
                   
                        if (vo.getSuccOrNot() == 0) {
                            if (log.isDebugEnabled()) {
                                log.debug("Failed to buyStock, caused by "
                                        + vo.getCause());
                            }
                            throw new StockAppBizException(vo.getCause());
                        }
                        if (vo.getSuccOrNot() == 1) {
                            if (log.isDebugEnabled()) {
                                log.debug("buyStock successfully.");
                            }
                        }
                    }
               
            }
        });
    }

    @Override
    public void sellStock(String acctID, String market, String code, String price, String amount) throws StockAppBizException {
        context.getService(ICommandExecutor.class).submitCommand(new SellStockCommand( acctID,  market,  code,  price,  amount), new IAsyncCallback() {
            @Override
            public void failed(Object cause) {
                log.error("sellStock fail" + cause.toString());
            }
            @Override
            public void success(Object result) {
                if (result != null && result instanceof StockResultVO) {
                    StockResultVO vo=(StockResultVO) result;
                   
                        if (vo.getSuccOrNot() == 0) {
                            if (log.isDebugEnabled()) {
                                log.debug("Failed sellStock, caused by "
                                        + vo.getCause());
                            }
                            throw new StockAppBizException(vo.getCause());
                        }
                        if (vo.getSuccOrNot() == 1) {
                            if (log.isDebugEnabled()) {
                                log.debug("Create sellStock successfully.");
                            }
                        }
                    }
               
            }
        });
    }
    @Override
    public void quickBuy(Long captitalAmount, String capitalRate, boolean virtual, String stockMarket, String stockCode, String stockBuyAmount, String depositRate) throws StockAppBizException {
        context.getService(ICommandExecutor.class).submitCommand(new QuickBuyStockCommand( captitalAmount,  capitalRate,  virtual,  stockMarket,  stockCode,  stockBuyAmount,  depositRate), new IAsyncCallback() {
            @Override
            public void failed(Object cause) {
                log.error("quickBuy fail" + cause.toString());
            }
            @Override
            public void success(Object result) {
                if (result != null && result instanceof StockResultVO) {
                    StockResultVO vo=(StockResultVO) result;
                   
                        if (vo.getSuccOrNot() == 0) {
                            if (log.isDebugEnabled()) {
                                log.debug("Failed quickBuy, caused by "
                                        + vo.getCause());
                            }
                            throw new StockAppBizException(vo.getCause());
                        }
                        if (vo.getSuccOrNot() == 1) {
                            if (log.isDebugEnabled()) {
                                log.debug("quickBuy successfully.");
                            }
                        }
                    }
               
            }
        });
    }
  //未结算
    @Override
    public TradingAccountBean getTradingAccountInfo(String acctID) throws StockAppBizException {
        if (tradingAccountBean_cache.getEntity(acctID)==null){
            TradingAccountBean b=new TradingAccountBean();
            b.setId(Long.valueOf(acctID));
            tradingAccountBean_cache.putEntity(b.getId(),b);
        }
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("acctID", acctID);
        this.tradingAccountBean_cache.forceReload(params,false);
        return tradingAccountBean_cache.getEntity(Long.valueOf(acctID));
    }

    @Override
    public void cancelOrder(String orderID) {
        
    }
	

}
