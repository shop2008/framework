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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.command.api.CommandConstraintViolatedException;
import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IServiceDecoratorBuilder;
import com.wxxr.mobile.core.microkernel.api.IServiceDelegateHolder;
import com.wxxr.mobile.core.microkernel.api.IStatefulService;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.session.api.ISessionManager;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordListBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.bean.VoucherDetailsBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.handler.ApplyDrawMoneyHandler;
import com.wxxr.mobile.stock.app.service.handler.ApplyDrawMoneyHandler.ApplyDrawMoneyCommand;
import com.wxxr.mobile.stock.app.service.loader.AuditDetailLoader;
import com.wxxr.mobile.stock.app.service.loader.DealDetailLoader;
import com.wxxr.mobile.stock.app.service.loader.EarnRankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.GainPayDetailsEntityLoader;
import com.wxxr.mobile.stock.app.service.loader.RegularTicketRankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.RightGainLoader;
import com.wxxr.mobile.stock.app.service.loader.T1RankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.TRankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.TradingAccountInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.TradingRecordLoader;
import com.wxxr.mobile.stock.app.service.loader.UserCreateTradAccInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.VoucherDetailsLoader;
import com.wxxr.mobile.stock.app.service.loader.WeekRankItemLoader;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.mobile.stock.trade.command.BuyStockCommand;
import com.wxxr.mobile.stock.trade.command.BuyStockHandler;
import com.wxxr.mobile.stock.trade.command.CancelOrderCommand;
import com.wxxr.mobile.stock.trade.command.CancelOrderHandler;
import com.wxxr.mobile.stock.trade.command.ClearTradingAccountCommand;
import com.wxxr.mobile.stock.trade.command.ClearTradingAccountHandler;
import com.wxxr.mobile.stock.trade.command.CreateTradingAccountCommand;
import com.wxxr.mobile.stock.trade.command.CreateTradingAccountHandler;
import com.wxxr.mobile.stock.trade.command.QuickBuyStockCommand;
import com.wxxr.mobile.stock.trade.command.QuickBuyStockHandler;
import com.wxxr.mobile.stock.trade.command.SellStockCommand;
import com.wxxr.mobile.stock.trade.command.SellStockHandler;
import com.wxxr.mobile.stock.trade.entityloader.TradingAccInfoLoader;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.GainVOs;
import com.wxxr.stock.trading.ejb.api.HomePageVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;

/**
 * 交易管理模块
 * 
 * @author wangxuyang
 * 
 */
public class TradingManagementServiceImpl extends AbstractModule<IStockAppContext> implements ITradingManagementService, IStatefulService {

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
			if(o1!=null && o2!=null){
				return Integer.valueOf(o2.getAcctId()) - Integer.valueOf(o1.getAcctId());
			}
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
	protected TradingAccountListBean myTradingAccounts;
	/**
	 * 我的交易盘详情
	 */
	protected TradingAccountBean myTradingAccount;
	/**
	 * 成交详情
	 */
	protected DealDetailBean dealDetailBean;
	/**
	 * 清算详情
	 */
	protected AuditDetailBean auditDetailBean;
	/**
	 * 创建交易盘的参数配置
	 */
	protected UserCreateTradAccInfoBean createTDConfig;
	/**
	 * 交易订单记录
	 */
	protected TradingRecordListBean recordsBean;
	
	//交易盘
    private GenericReloadableEntityCache<String,TradingAccInfoBean,List<TradingAccInfoBean>> tradingAccInfo_cache;
    //交易盘详细信息
    private GenericReloadableEntityCache<Long,TradingAccountBean,List<TradingAccountBean>> tradingAccountBean_cache;
    protected UserCreateTradAccInfoBean userCreateTradAccInfo;
    private IReloadableEntityCache<String, UserCreateTradAccInfoBean> userCreateTradAccInfo_Cache;
    
    private GenericReloadableEntityCache<String,DealDetailBean,List<DealDetailBean>> dealDetailBean_cache;
    private GenericReloadableEntityCache<String,AuditDetailBean,List<AuditDetailBean>> auditDetailBean_cache;
    private GenericReloadableEntityCache<String,VoucherDetailsBean,List<VoucherDetailsBean>> voucherDetailsBean_cache;
    
    private GenericReloadableEntityCache<String,GainPayDetailBean,List<GainPayDetailBean>> gainPayDetailBean_cache;

    protected BindableListWrapper<GainPayDetailBean> vgainPayDetails;


	// =================module life cycle methods=============================
	@Override
	public void startService() {
		IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
		registry.registerEntityLoader("earnRank", new EarnRankItemLoader());
		registry.registerEntityLoader("tRank", new TRankItemLoader());
		registry.registerEntityLoader("t1Rank", new T1RankItemLoader());
		registry.registerEntityLoader("weekRank", new WeekRankItemLoader());
		registry.registerEntityLoader("rtRank", new RegularTicketRankItemLoader());
		registry.registerEntityLoader("rightTotalGain", new RightGainLoader());
        registry.registerEntityLoader("tradingAccInfo", new TradingAccInfoLoader());
        registry.registerEntityLoader("UserCreateTradAccInfo", new UserCreateTradAccInfoLoader());
        registry.registerEntityLoader("TradingAccountInfo", new TradingAccountInfoLoader());
        registry.registerEntityLoader("tradingRecordBean", new TradingRecordLoader());
        registry.registerEntityLoader("dealDetailBean", new DealDetailLoader());
        registry.registerEntityLoader("auditDetailBean", new AuditDetailLoader());
        registry.registerEntityLoader("voucherDetailsBean", new VoucherDetailsLoader());
        registry.registerEntityLoader("vgainPayDetails", new GainPayDetailsEntityLoader());


        context.getService(ICommandExecutor.class).registerCommandHandler(CreateTradingAccountCommand.Name, new CreateTradingAccountHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(BuyStockCommand.Name, new BuyStockHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(SellStockCommand.Name, new SellStockHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(QuickBuyStockCommand.Name, new QuickBuyStockHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(CancelOrderCommand.Name, new CancelOrderHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(ClearTradingAccountCommand.Name, new ClearTradingAccountHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(ApplyDrawMoneyHandler.COMMAND_NAME, new ApplyDrawMoneyHandler());

		context.registerService(ITradingManagementService.class, this);
	}

	/**
	 * 
	 */
	protected void doInit() {
		myTradingAccounts = new TradingAccountListBean();
		myTradingAccount = new TradingAccountBean();
		dealDetailBean = new DealDetailBean();
		auditDetailBean = new AuditDetailBean();
		createTDConfig = new UserCreateTradAccInfoBean();
		recordsBean = new TradingRecordListBean();
		tradingAccInfo_cache=new GenericReloadableEntityCache<String,TradingAccInfoBean,List<TradingAccInfoBean>>("tradingAccInfo",30);
        tradingAccountBean_cache=new GenericReloadableEntityCache<Long, TradingAccountBean, List<TradingAccountBean>>("TradingAccountInfo",30);
        tradingRecordBean_cache=new  GenericReloadableEntityCache<Long, TradingRecordBean, List<TradingRecordBean>>("tradingRecordBean");
        dealDetailBean_cache=new  GenericReloadableEntityCache<String,DealDetailBean,List<DealDetailBean>> ("dealDetailBean");
        auditDetailBean_cache=new  GenericReloadableEntityCache<String,AuditDetailBean,List<AuditDetailBean>> ("auditDetailBean");
        voucherDetailsBean_cache=new GenericReloadableEntityCache<String,VoucherDetailsBean,List<VoucherDetailsBean>>("voucherDetailsBean");
	}

	@Override
	public void stopService() {
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
			this.earnRank = this.earnRankCache.getEntities(null, earnRankComparator);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		this.earnRankCache.doReloadIfNeccessay(params);
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
	    if (dealDetailBean_cache.getEntity(acctID)==null){
	        DealDetailBean b=new DealDetailBean();
            dealDetailBean_cache.putEntity(acctID,b);
        }
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("acctID", acctID);
        this.dealDetailBean_cache.forceReload(params,false);
        return dealDetailBean_cache.getEntity(acctID);
	}
	
	public AuditDetailBean getAuditDetail(final String acctId) {
	    if (auditDetailBean_cache.getEntity(acctId)==null){
	        AuditDetailBean b=new AuditDetailBean();
            auditDetailBean_cache.putEntity(acctId,b);
        }
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("acctID", acctId);
        this.auditDetailBean_cache.forceReload(params,false);
        return auditDetailBean_cache.getEntity(acctId);
	}
	


	@Override
	public void clearTradingAccount(final String acctID) {
		try {
			Future<StockResultVO> f = context.getService(ICommandExecutor.class).submitCommand(new ClearTradingAccountCommand( acctID));
			Object result = f.get();
			if (result != null && result instanceof StockResultVO) {
	            StockResultVO vo=(StockResultVO) result;
	                if (vo.getSuccOrNot() == 0) {
	                    if (log.isDebugEnabled()) {
	                        log.debug("Failed to clearTradingAccount, caused by "
	                                + vo.getCause());
	                    }
	                    throw new StockAppBizException(vo.getCause());
	                }
	                if (vo.getSuccOrNot() == 1) {
	                    if (log.isDebugEnabled()) {
	                        log.debug("clearTradingAccount successfully.");
	                    }
	                    tradingAccountBean_cache.forceReload(true);
	                }
	            }
			
		} catch (InterruptedException e) {
			throw new StockAppBizException("系统清仓失败");
		} catch (ExecutionException e) {
			Throwable t = e.getCause();
            if( t instanceof CommandConstraintViolatedException){
                throw (CommandConstraintViolatedException)t;
            }else if(t instanceof StockAppBizException){
            	throw (StockAppBizException)t;
            }else{
            	throw new StockAppBizException("系统清仓失败");
            }
		}
	}
	@Override
	public TradingAccountListBean getMyAllTradingAccountList(final int start,
			final int limit) {

		List<GainVO> volist = null;
		try {
			volist = fetchDataFromServer(new Callable<List<GainVO>>() {
				public List<GainVO> call() throws Exception {
					try {
						if (log.isDebugEnabled()) {
							log.debug("fetch all trading account info...");
						}
						GainVOs vos=getRestService(ITradingProtectedResource.class)
								.getTotalGain(start, limit);
						List<GainVO> list =vos==null?null:vos.getGains() ;
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

		List<GainVO> volist = null;
		try {
			volist = fetchDataFromServer(new Callable<List<GainVO>>() {
				public List<GainVO> call() throws Exception {
					try {
						if (log.isDebugEnabled()) {
							log.debug("fetch all trading account info...");
						}
						GainVOs vos=getRestService(ITradingProtectedResource.class)
								.getTotalGain(start, limit);
						List<GainVO> list =vos==null?null:vos.getGains() ;
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
		}
		return myTradingAccounts;
	}
	//交易记录
    private GenericReloadableEntityCache<Long,TradingRecordBean,List<TradingRecordBean>> tradingRecordBean_cache;

	public BindableListWrapper<TradingRecordBean> getTradingAccountRecord(final String acctID,
            final int start, final int limit){
        BindableListWrapper<TradingRecordBean> tradingRecordBeans = tradingRecordBean_cache.getEntities(new IEntityFilter<TradingRecordBean>(){
               @Override
               public boolean doFilter(TradingRecordBean entity) {
                   if ( entity.getAcctID().equals(acctID)){
                       return true;
                   }
                   return false;
               }
               
           }, new TradingRecordBeanComparator());
        
         Map<String, Object> p=new HashMap<String, Object>(); 
         p.put("acctID", acctID);
         p.put("start", start);
         p.put("limit", limit);
         tradingRecordBean_cache.forceReload(p,false);
         tradingRecordBean_cache.setCommandParameters(p);
	    return tradingRecordBeans;
	}
	class TradingRecordBeanComparator implements Comparator<TradingRecordBean>{
        @Override
        public int compare(TradingRecordBean b1, TradingRecordBean b2) {
          return b1.getDate()>b2.getDate()?-1:1;
        }
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
				this.rightTotalGain = getRightTotalGainCache().getEntities(null, new Comparator<GainBean>(){
					@Override
					public int compare(GainBean o1, GainBean o2) {
						return o2.getCloseTime().compareTo(o1.getCloseTime());
					}

				});
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
			} , new Comparator<GainBean>(){
				@Override
				public int compare(GainBean o1, GainBean o2) {
					return o2.getCloseTime().compareTo(o1.getCloseTime());
				}
			});
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
	
	
	//获取我的T日 和 T+1日交易盘
	public BindableListWrapper<TradingAccInfoBean> getAllTradingAccountList(){
    	tradingAccInfo_cache.clear();
        tradingAccInfo_cache.forceReload(false);
        BindableListWrapper<TradingAccInfoBean> allT = tradingAccInfo_cache.getEntities(null, new  TradingAccInfoBeanComparator());
        return allT;		
	}
	
  
    //获取我的T日交易盘
    public BindableListWrapper<TradingAccInfoBean> getT0TradingAccountList(){
    	tradingAccInfo_cache.clear();
        tradingAccInfo_cache.forceReload(false);
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
    	tradingAccInfo_cache.clear();
        tradingAccInfo_cache.forceReload(false);
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
    public void createTradingAccount(Long captitalAmount, float capitalRate, boolean virtual, float depositRate,String assetType) throws StockAppBizException {
		try {
			Future<StockResultVO> f = context.getService(ICommandExecutor.class).submitCommand(new CreateTradingAccountCommand(captitalAmount,  capitalRate,  virtual,  depositRate,assetType));
			Object result = f.get();
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
		}catch (ExecutionException e) {
			Throwable t = e.getCause();
            if( t instanceof CommandConstraintViolatedException){
                throw (CommandConstraintViolatedException)t;
            }else if(t instanceof StockAppBizException){
            	throw (StockAppBizException)t;
            }else{
                throw new StockAppBizException("创建交易盘失败");
            }
		} catch (InterruptedException e){
			throw new StockAppBizException("创建交易盘失败");
		}
    }
    
    @Override
    public void buyStock(String acctID, String market, String code, String price, String amount) throws StockAppBizException {
        try {
        	Future<StockResultVO> f=  context.getService(ICommandExecutor.class).submitCommand(new BuyStockCommand( acctID,  market,  code,  price,  amount));
        	StockResultVO result= f.get();
           if (result != null ) {
               StockResultVO vo=result;
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
                       tradingAccountBean_cache.forceReload(true);
                   }
               }
        }catch (ExecutionException e) {
			Throwable t = e.getCause();
            if( t instanceof CommandConstraintViolatedException){
                throw (CommandConstraintViolatedException)t;
            }else if(t instanceof StockAppBizException){
            	throw (StockAppBizException)t;
            }else{
                throw new StockAppBizException("买人股票失败");
            }
		} catch (InterruptedException e){
			throw new StockAppBizException("买人股票失败");
		}      
    }

    @Override
    public void sellStock(String acctID, String market, String code, String price, String amount) throws StockAppBizException {
		try {
			Future<StockResultVO> f = context.getService(ICommandExecutor.class).submitCommand(new SellStockCommand( acctID,  market,  code,  price,  amount));
			StockResultVO result = f.get();
	    	if (result != null) {
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
	                    tradingAccountBean_cache.forceReload(true);
	                }
	            }			
		}catch (ExecutionException e) {
			Throwable t = e.getCause();
            if( t instanceof CommandConstraintViolatedException){
                throw (CommandConstraintViolatedException)t;
            }else if(t instanceof StockAppBizException){
            	throw (StockAppBizException)t;
            }else{
                throw new StockAppBizException("卖出股票失败");
            }
		} catch (InterruptedException e){
			throw new StockAppBizException("卖出股票失败");
		}      
    }
    @Override
    public void quickBuy(Long captitalAmount, String capitalRate, boolean virtual, String stockMarket, String stockCode, String stockBuyAmount, String depositRate,String assetType ) throws StockAppBizException {
    	try {
    		Future<StockResultVO> f = context.getService(ICommandExecutor.class).submitCommand(new QuickBuyStockCommand( captitalAmount,  capitalRate,  virtual,  stockMarket,  stockCode,  stockBuyAmount,  depositRate,assetType));
    		StockResultVO result = f.get();
			if (result != null) {
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
                      tradingAccountBean_cache.forceReload(true);
                  }
              }
		} catch (InterruptedException e) {
			 throw new StockAppBizException("买入股票失败");
		} catch (ExecutionException e) {
			Throwable t = e.getCause();
            if( t instanceof CommandConstraintViolatedException){
                throw (CommandConstraintViolatedException)t;
            }else if(t instanceof StockAppBizException){
            	throw (StockAppBizException)t;
            }else{
                throw new StockAppBizException("买入股票失败");
            }
		}
    }
  //未结算
    @Override
    public TradingAccountBean getTradingAccountInfo(String acctID) throws StockAppBizException {
    	if (!StringUtils.isNumeric(acctID)) {
			throw new IllegalArgumentException("Invalid Argument:accId===>"+acctID);
		}
    	Long accId =  Long.valueOf(acctID);
        if (tradingAccountBean_cache.getEntity(accId)==null){
            TradingAccountBean b=new TradingAccountBean();
            b.setId(accId);
            tradingAccountBean_cache.putEntity(b.getId(),b);
        }
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("acctID", acctID);
        this.tradingAccountBean_cache.forceReload(params,false);
        return tradingAccountBean_cache.getEntity(Long.valueOf(acctID));
    }

    @Override
    public void cancelOrder(String orderID) {
		try {
			Future<StockResultVO> f = context.getService(ICommandExecutor.class).submitCommand(new CancelOrderCommand( orderID));
			Object result = f.get();
	    	if (result != null && result instanceof StockResultVO) {
	            StockResultVO vo=(StockResultVO) result;
	           
	                if (vo.getSuccOrNot() == 0) {
	                    if (log.isDebugEnabled()) {
	                        log.debug("Failed to cancelOrder, caused by "
	                                + vo.getCause());
	                    }
	                    throw new StockAppBizException(vo.getCause());
	                }
	                if (vo.getSuccOrNot() == 1) {
	                    if (log.isDebugEnabled()) {
	                        log.debug("cancelOrder successfully.");
	                    }
	                    tradingAccountBean_cache.forceReload(true);
	                }
	            }			
		} catch (InterruptedException e) {
			throw new StockAppBizException("系统执行撤单失败");
		} catch (ExecutionException e) {
			Throwable t = e.getCause();
            if( t instanceof CommandConstraintViolatedException){
                throw (CommandConstraintViolatedException)t;
            }else if(t instanceof StockAppBizException){
            	throw (StockAppBizException)t;
            }else{
            	throw new StockAppBizException("系统执行撤单失败");
            }
		}
    }

	@Override
	public void applyDrawMoney(long amount) {
		ApplyDrawMoneyCommand cmd=new ApplyDrawMoneyCommand();
		cmd.setAmount(amount);
		try{
		Future<StockResultVO> future=context.getService(ICommandExecutor.class).submitCommand(cmd);
			try {
				StockResultVO vo=future.get(30,TimeUnit.SECONDS);
				if(vo.getSuccOrNot()!=1){
					throw new StockAppBizException(vo.getCause());
				}
			} catch(StockAppBizException e){
				throw e;
			}catch (Exception e) {
				throw new StockAppBizException("系统错误");
			}
		}catch(CommandException e){
			throw new StockAppBizException(e.getMessage());
		}
	}

	public VoucherDetailsBean getVoucherDetails(int start, int limit){
	    String key="key";
	    if (voucherDetailsBean_cache.getEntity(key)==null){
	        VoucherDetailsBean b=new VoucherDetailsBean();
	        voucherDetailsBean_cache.putEntity(key,b);
        }
	    Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", start);
        params.put("limit", limit);
        this.voucherDetailsBean_cache.forceReload(params, false);
        return voucherDetailsBean_cache.getEntity(key);
	}
	private static final Comparator<GainPayDetailBean> vgainPayDetailsComparator = new Comparator<GainPayDetailBean>() {
		
		@Override
		public int compare(GainPayDetailBean o1, GainPayDetailBean o2) {
			if(o1!=null && o2!=null){
				return (int) (o2.getTime() - o1.getTime());
			}
			return 0;
		}
	};
	public BindableListWrapper<GainPayDetailBean> getGainPayDetailDetails(int start, int limit){
		if (log.isDebugEnabled()) {
			log.debug(String.format("params:[start=%s,limit=%s]", start,limit));
		}
		if(this.vgainPayDetails == null){
			if(this.gainPayDetailBean_cache == null){
				this.gainPayDetailBean_cache = new  GenericReloadableEntityCache<String,GainPayDetailBean,List<GainPayDetailBean>>("vgainPayDetails");
			}
			this.vgainPayDetails = this.gainPayDetailBean_cache.getEntities(null, vgainPayDetailsComparator);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		this.gainPayDetailBean_cache.doReloadIfNeccessay(params);
		return this.vgainPayDetails;
	}
	
	void clearCache(){
		tradingAccInfo_cache.clear();
        tradingAccountBean_cache.clear();
        tradingRecordBean_cache.clear();
        dealDetailBean_cache.clear();
        auditDetailBean_cache.clear();
        voucherDetailsBean_cache.clear();
	}


	protected <S> S getService(Class<S> clazz) {
		return this.context.getService(clazz);
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(ISessionManager.class);
		addRequiredService(IRestProxyService.class);
		addRequiredService(IEntityLoaderRegistry.class);
	    addRequiredService(IUserIdentityManager.class);
	}

	@Override
	public void destroy(Object serviceHandler) {
		
	}

	@Override
	public IServiceDecoratorBuilder getDecoratorBuilder() {
		return new IServiceDecoratorBuilder() {
			
			@Override
			public <T> T createServiceDecorator(Class<T> clazz,
					final IServiceDelegateHolder<T> holder) {
				if(clazz == ITradingManagementService.class){
					return clazz.cast(new ITradingManagementService(){
						

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getT0TradingAccountList()
						 */
						public BindableListWrapper<TradingAccInfoBean> getT0TradingAccountList() {
							return ((ITradingManagementService)holder.getDelegate()).getT0TradingAccountList();
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getT1TradingAccountList()
						 */
						public BindableListWrapper<TradingAccInfoBean> getT1TradingAccountList() {
							return ((ITradingManagementService)holder.getDelegate()).getT1TradingAccountList();
						}

						/**
						 * @param strat
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getMyAllTradingAccountList(int, int)
						 */
						public TradingAccountListBean getMyAllTradingAccountList(
								int strat, int limit) {
							return ((ITradingManagementService)holder.getDelegate()).getMyAllTradingAccountList(strat,
									limit);
						}

						/**
						 * @param strat
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getMySuccessTradingAccountList(int, int)
						 */
						public TradingAccountListBean getMySuccessTradingAccountList(
								int strat, int limit) {
							return ((ITradingManagementService)holder.getDelegate()).getMySuccessTradingAccountList(
									strat, limit);
						}

						/**
						 * @param acctID
						 * @return
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getTradingAccountInfo(java.lang.String)
						 */
						public TradingAccountBean getTradingAccountInfo(
								String acctID) throws StockAppBizException {
							return ((ITradingManagementService)holder.getDelegate()).getTradingAccountInfo(acctID);
						}

						/**
						 * @param captitalAmount
						 * @param capitalRate
						 * @param virtual
						 * @param depositRate
						 * @param assetType
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#createTradingAccount(java.lang.Long, float, boolean, float, java.lang.String)
						 */
						public void createTradingAccount(Long captitalAmount,
								float capitalRate, boolean virtual,
								float depositRate, String assetType)
								throws StockAppBizException {
							((ITradingManagementService)holder.getDelegate()).createTradingAccount(captitalAmount,
									capitalRate, virtual, depositRate,
									assetType);
						}

						/**
						 * @param acctID
						 * @param market
						 * @param code
						 * @param price
						 * @param amount
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#buyStock(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
						 */
						public void buyStock(String acctID, String market,
								String code, String price, String amount)
								throws StockAppBizException {
							((ITradingManagementService)holder.getDelegate()).buyStock(acctID, market, code, price,
									amount);
						}

						/**
						 * @param acctID
						 * @param market
						 * @param code
						 * @param price
						 * @param amount
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#sellStock(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
						 */
						public void sellStock(String acctID, String market,
								String code, String price, String amount)
								throws StockAppBizException {
							((ITradingManagementService)holder.getDelegate()).sellStock(acctID, market, code, price,
									amount);
						}

						/**
						 * @param orderID
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#cancelOrder(java.lang.String)
						 */
						public void cancelOrder(String orderID) {
							((ITradingManagementService)holder.getDelegate()).cancelOrder(orderID);
						}

						/**
						 * @param acctID
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#clearTradingAccount(java.lang.String)
						 */
						public void clearTradingAccount(String acctID) {
							((ITradingManagementService)holder.getDelegate()).clearTradingAccount(acctID);
						}

						/**
						 * @param captitalAmount
						 * @param capitalRate
						 * @param virtual
						 * @param stockMarket
						 * @param stockCode
						 * @param stockBuyAmount
						 * @param depositRate
						 * @param assetType
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#quickBuy(java.lang.Long, java.lang.String, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
						 */
						public void quickBuy(Long captitalAmount,
								String capitalRate, boolean virtual,
								String stockMarket, String stockCode,
								String stockBuyAmount, String depositRate,
								String assetType) throws StockAppBizException {
							((ITradingManagementService)holder.getDelegate()).quickBuy(captitalAmount, capitalRate,
									virtual, stockMarket, stockCode,
									stockBuyAmount, depositRate, assetType);
						}

						/**
						 * @param accId
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getDealDetail(java.lang.String)
						 */
						public DealDetailBean getDealDetail(String accId) {
							return ((ITradingManagementService)holder.getDelegate()).getDealDetail(accId);
						}

						/**
						 * @param accId
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getAuditDetail(java.lang.String)
						 */
						public AuditDetailBean getAuditDetail(String accId) {
							return ((ITradingManagementService)holder.getDelegate()).getAuditDetail(accId);
						}

						/**
						 * @param start
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getEarnRank(int, int)
						 */
						public BindableListWrapper<EarnRankItemBean> getEarnRank(
								int start, int limit) {
							return ((ITradingManagementService)holder.getDelegate()).getEarnRank(start, limit);
						}

						/**
						 * @param start
						 * @param limit
						 * @param wait4Finish
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadEarnRank(int, int, boolean)
						 */
						public void reloadEarnRank(int start, int limit,
								boolean wait4Finish) {
							((ITradingManagementService)holder.getDelegate()).reloadEarnRank(start, limit, wait4Finish);
						}

						/**
						 * @return
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getTMegagameRank()
						 */
						public BindableListWrapper<MegagameRankBean> getTMegagameRank()
								throws StockAppBizException {
							return ((ITradingManagementService)holder.getDelegate()).getTMegagameRank();
						}

						/**
						 * @param wait4Finish
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadTMegagameRank(boolean)
						 */
						public void reloadTMegagameRank(boolean wait4Finish) {
							((ITradingManagementService)holder.getDelegate()).reloadTMegagameRank(wait4Finish);
						}

						/**
						 * @return
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getT1MegagameRank()
						 */
						public BindableListWrapper<MegagameRankBean> getT1MegagameRank()
								throws StockAppBizException {
							return ((ITradingManagementService)holder.getDelegate()).getT1MegagameRank();
						}

						/**
						 * @param wait4Finish
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadT1MegagameRank(boolean)
						 */
						public void reloadT1MegagameRank(boolean wait4Finish) {
							((ITradingManagementService)holder.getDelegate()).reloadT1MegagameRank(wait4Finish);
						}

						/**
						 * @return
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getRegularTicketRank()
						 */
						public BindableListWrapper<RegularTicketBean> getRegularTicketRank()
								throws StockAppBizException {
							return ((ITradingManagementService)holder.getDelegate()).getRegularTicketRank();
						}

						/**
						 * @param wait4Finish
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadRegularTicketRank(boolean)
						 */
						public void reloadRegularTicketRank(boolean wait4Finish) {
							((ITradingManagementService)holder.getDelegate()).reloadRegularTicketRank(wait4Finish);
						}

						/**
						 * @return
						 * @throws StockAppBizException
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getWeekRank()
						 */
						public BindableListWrapper<WeekRankBean> getWeekRank()
								throws StockAppBizException {
							return ((ITradingManagementService)holder.getDelegate()).getWeekRank();
						}

						/**
						 * @param wait4Finish
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadWeekRank(boolean)
						 */
						public void reloadWeekRank(boolean wait4Finish) {
							((ITradingManagementService)holder.getDelegate()).reloadWeekRank(wait4Finish);
						}

						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getUserCreateTradAccInfo()
						 */
						public UserCreateTradAccInfoBean getUserCreateTradAccInfo() {
							return ((ITradingManagementService)holder.getDelegate()).getUserCreateTradAccInfo();
						}

						/**
						 * @param acctID
						 * @param start
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getTradingAccountRecord(java.lang.String, int, int)
						 */
						public BindableListWrapper<TradingRecordBean> getTradingAccountRecord(
								String acctID, int start, int limit) {
							return ((ITradingManagementService)holder.getDelegate()).getTradingAccountRecord(acctID,
									start, limit);
						}

						/**
						 * @param start
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getTotalGain(int, int)
						 */
						public BindableListWrapper<GainBean> getTotalGain(
								int start, int limit) {
							return ((ITradingManagementService)holder.getDelegate()).getTotalGain(start, limit);
						}

						/**
						 * @param start
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getGain(int, int)
						 */
						public BindableListWrapper<GainBean> getGain(int start,
								int limit) {
							return ((ITradingManagementService)holder.getDelegate()).getGain(start, limit);
						}

						/**
						 * @param amount
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#applyDrawMoney(long)
						 */
						public void applyDrawMoney(long amount) {
							((ITradingManagementService)holder.getDelegate()).applyDrawMoney(amount);
						}

						/**
						 * @param start
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getVoucherDetails(int, int)
						 */
						public VoucherDetailsBean getVoucherDetails(int start,
								int limit) {
							return ((ITradingManagementService)holder.getDelegate()).getVoucherDetails(start, limit);
						}

						/**
						 * @param start
						 * @param limit
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getGainPayDetailDetails(int, int)
						 */
						public BindableListWrapper<GainPayDetailBean> getGainPayDetailDetails(
								int start, int limit) {
							return ((ITradingManagementService)holder.getDelegate()).getGainPayDetailDetails(start,
									limit);
						}

						public BindableListWrapper<TradingAccInfoBean> getAllTradingAccountList() {
							return ((ITradingManagementService)holder.getDelegate()).getAllTradingAccountList();
						}
						
						
						
					});
				}else{
					throw new IllegalArgumentException("Invalid service class :"+clazz);
				}
			}
		};
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		try {
			TradingManagementServiceImpl impl = (TradingManagementServiceImpl)super.clone();
			impl.doInit();
			return impl;
		}catch (CloneNotSupportedException e) {
			throw new RuntimeException("SHOULD NOT HAPPEN !");
		}
	}
}

