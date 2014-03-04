/**
 * 
 */
package com.wxxr.mobile.stock.app.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.mobile.core.async.api.AsyncFuture;
import com.wxxr.mobile.core.async.api.DelegateCallback;
import com.wxxr.mobile.core.async.api.ExecAsyncException;
import com.wxxr.mobile.core.async.api.IAsyncCallable;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.IDataConverter;
import com.wxxr.mobile.core.async.api.NestedRuntimeException;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IServiceDecoratorBuilder;
import com.wxxr.mobile.core.microkernel.api.IServiceDelegateHolder;
import com.wxxr.mobile.core.microkernel.api.IStatefulService;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.session.api.ISessionManager;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.DrawMoneyRecordBean;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.bean.GainPayDetailBean;
import com.wxxr.mobile.stock.app.bean.HomePageMenu;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;
import com.wxxr.mobile.stock.app.bean.StockTradingOrderBean;
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.bean.TradingConfigBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordListBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.bean.VoucherDetailsBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.command.BuyStockCommand;
import com.wxxr.mobile.stock.app.command.CancelOrderCommand;
import com.wxxr.mobile.stock.app.command.ClearTradingAccountCommand;
import com.wxxr.mobile.stock.app.command.CreateTradingAccountCommand;
import com.wxxr.mobile.stock.app.command.QuickBuyStockCommand;
import com.wxxr.mobile.stock.app.command.SellStockCommand;
import com.wxxr.mobile.stock.app.common.AsyncUtils;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IBindableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFetcher;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.service.IMessageManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.handler.ApplyDrawMoneyCommand;
import com.wxxr.mobile.stock.app.service.handler.ApplyDrawMoneyHandler;
import com.wxxr.mobile.stock.app.service.handler.BuyStockHandler;
import com.wxxr.mobile.stock.app.service.handler.CancelOrderHandler;
import com.wxxr.mobile.stock.app.service.handler.ClearTradingAccountHandler;
import com.wxxr.mobile.stock.app.service.handler.CreateTradingAccountHandler;
import com.wxxr.mobile.stock.app.service.handler.QuickBuyStockHandler;
import com.wxxr.mobile.stock.app.service.handler.SellStockHandler;
import com.wxxr.mobile.stock.app.service.loader.AuditDetailLoader;
import com.wxxr.mobile.stock.app.service.loader.DealDetailLoader;
import com.wxxr.mobile.stock.app.service.loader.DrawMoneyRecordLoader;
import com.wxxr.mobile.stock.app.service.loader.EarnRankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.GainPayDetailsEntityLoader;
import com.wxxr.mobile.stock.app.service.loader.RegularTicketRankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.RightGainLoader;
import com.wxxr.mobile.stock.app.service.loader.T1RankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.TRankItemLoader;
import com.wxxr.mobile.stock.app.service.loader.TradingAccountInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.TradingConfigInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.TradingRecordLoader;
import com.wxxr.mobile.stock.app.service.loader.UserCreateTradAccInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.VoucherDetailsLoader;
import com.wxxr.mobile.stock.app.service.loader.WeekRankItemLoader;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.mobile.stock.app.v2.bean.BaseMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.ChampionShipMessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.MessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.SignInMessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.TradingAccountMenuItem;
import com.wxxr.mobile.stock.trade.entityloader.TradingAccInfoLoader;
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.AppHomePageListVO;
import com.wxxr.stock.trading.ejb.api.DrawMoneyRecordVo;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.GainVOs;
import com.wxxr.stock.trading.ejb.api.HomePageVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankNUpdateTimeVO;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;
import com.wxxr.stock.trading.ejb.api.PullMessageVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.SecurityAppHomePageVO;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;
import com.wxxr.stock.trading.ejb.api.TradingConfigVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;
import com.wxxr.stock.trading.ejb.api.UserSignVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;

/**
 * 交易管理模块
 * 
 * @author wangxuyang
 * 
 */
public class TradingManagementServiceImpl extends AbstractModule<IStockAppContext> implements ITradingManagementService, IStatefulService {

	private static final Trace log = Trace.register(TradingManagementServiceImpl.class);
	private Timer timer = new Timer("Cache Clear Thread");
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
	private GenericReloadableEntityCache<String, GainBean,GainVO> leftSuccessGainCache;
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
    private IReloadableEntityCache<String, TradingConfigBean> tradingConfig_Cache;
    
    private GenericReloadableEntityCache<String,DealDetailBean,List<DealDetailBean>> dealDetailBean_cache;
    private GenericReloadableEntityCache<String,AuditDetailBean,List<AuditDetailBean>> auditDetailBean_cache;
    private GenericReloadableEntityCache<String,VoucherDetailsBean,List<VoucherDetailsBean>> voucherDetailsBean_cache;
    
    private GenericReloadableEntityCache<String,GainPayDetailBean,List<GainPayDetailBean>> gainPayDetailBean_cache;

    protected BindableListWrapper<GainPayDetailBean> vgainPayDetails;
    //提现
    private GenericReloadableEntityCache<Long,DrawMoneyRecordBean,List<DrawMoneyRecordVo>> drawMoneyRecordBean_cache;

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
		registry.registerEntityLoader("leftSuccessGain", new RightGainLoader());
        registry.registerEntityLoader("tradingAccInfo", new TradingAccInfoLoader());
        registry.registerEntityLoader("UserCreateTradAccInfo", new UserCreateTradAccInfoLoader());
        registry.registerEntityLoader("TradingAccountInfo", new TradingAccountInfoLoader());
        registry.registerEntityLoader("tradingRecordBean", new TradingRecordLoader());
        registry.registerEntityLoader("dealDetailBean", new DealDetailLoader());
        registry.registerEntityLoader("auditDetailBean", new AuditDetailLoader());
        registry.registerEntityLoader("voucherDetailsBean", new VoucherDetailsLoader());
        registry.registerEntityLoader("vgainPayDetails", new GainPayDetailsEntityLoader());
        registry.registerEntityLoader("drawMoneyRecordBean", new DrawMoneyRecordLoader());
        registry.registerEntityLoader("tradingConfigBean", new TradingConfigInfoLoader());

        context.getService(ICommandExecutor.class).registerCommandHandler(CreateTradingAccountCommand.Name, new CreateTradingAccountHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(BuyStockCommand.Name, new BuyStockHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(SellStockCommand.Name, new SellStockHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(QuickBuyStockCommand.Name, new QuickBuyStockHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(CancelOrderCommand.Name, new CancelOrderHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(ClearTradingAccountCommand.Name, new ClearTradingAccountHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(ApplyDrawMoneyHandler.COMMAND_NAME, new ApplyDrawMoneyHandler());
        Calendar calendar = Calendar.getInstance();  
        // 指定01:00:00点执行  
        calendar.set(Calendar.HOUR_OF_DAY, 8);  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND, 0);  
        Date date = calendar.getTime();  
        timer.schedule(new TimerTask() {
			@Override
			public void run() {
				clearCache();
			}
		}, date, 24*60*60*1000);
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
		tradingAccInfo_cache=new GenericReloadableEntityCache<String,TradingAccInfoBean,List<TradingAccInfoBean>>("tradingAccInfo",60);
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
		AsyncUtils.forceLoadNFetchAsyncInUI(this.earnRankCache, params, new AsyncFuture<BindableListWrapper<EarnRankItemBean>>(), new IEntityFetcher<BindableListWrapper<EarnRankItemBean>>() {

			public BindableListWrapper fetchFromCache(IBindableEntityCache cache) {
				return earnRank;
			}
		});
		return this.earnRank;
	}

	public synchronized BindableListWrapper<MegagameRankBean> getTMegagameRank() throws StockAppBizException {
		if(this.tRank == null){
			if(this.tRankCache == null){
				this.tRankCache = new GenericReloadableEntityCache<String, MegagameRankBean, MegagameRankVO>("tRank");
			}
			this.tRank = this.tRankCache.getEntities(null, tRankComparator);
		}
		this.tRankCache.doReload(false,null,null);
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
		this.t1RankCache.doReload(false,null,null);
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
		this.rtRankCache.doReload(false,null,null);
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
		this.weekRankCache.doReload(false,null,null);
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
        this.dealDetailBean_cache.doReload(true,params,null);;
        return dealDetailBean_cache.getEntity(acctID);
	}
	
	public AuditDetailBean getAuditDetail(final String acctId) {
	    if (auditDetailBean_cache.getEntity(acctId)==null){
	        AuditDetailBean b=new AuditDetailBean();
            auditDetailBean_cache.putEntity(acctId,b);
        }
        Map<String, Object> params=new HashMap<String, Object>(); 
        params.put("acctID", acctId);
        this.auditDetailBean_cache.doReload(true,params,null);;
        return auditDetailBean_cache.getEntity(acctId);
	}
	


	@Override
	public void clearTradingAccount(final String acctID) {
		AsyncFuture<StockResultVO> future = new AsyncFuture<StockResultVO>(){

			/* (non-Javadoc)
			 * @see com.wxxr.mobile.core.async.api.AsyncFuture#getInternalCallback()
			 */
			@Override
			public IAsyncCallback<StockResultVO> getInternalCallback() {
				return new DelegateCallback<StockResultVO, StockResultVO>(super.getInternalCallback()) {

					@Override
					protected StockResultVO getTargetValue(StockResultVO value) {
						return value;
					}

					/* (non-Javadoc)
					 * @see com.wxxr.mobile.core.async.api.DelegateCallback#success(java.lang.Object)
					 */
					@Override
					public void success(StockResultVO result) {
	                    if (log.isDebugEnabled()) {
	                        log.debug("clearTradingAccount successfully.");
	                    }
						AsyncUtils.forceLoadInSync(tradingAccountBean_cache, null);
						super.success(result);
					}
				};
			}
			
		};
		context.getService(ICommandExecutor.class).submitCommand(new ClearTradingAccountCommand( acctID),future.getInternalCallback());
		throw new ExecAsyncException(future);
//		try {
//			Future<StockResultVO> f = context.getService(ICommandExecutor.class).submitCommand(new ClearTradingAccountCommand( acctID));
//			Object result = f.get();
//			if (result != null && result instanceof StockResultVO) {
//	            StockResultVO vo=(StockResultVO) result;
//	                if (vo.getSuccOrNot() == 0) {
//	                    if (log.isDebugEnabled()) {
//	                        log.debug("Failed to clearTradingAccount, caused by "
//	                                + vo.getCause());
//	                    }
//	                    throw new StockAppBizException(vo.getCause());
//	                }
//	                if (vo.getSuccOrNot() == 1) {
//	                    if (log.isDebugEnabled()) {
//	                        log.debug("clearTradingAccount successfully.");
//	                    }
//	                    tradingAccountBean_cache.forceReload(true);
//	                }
//	            }
//			
//		} catch (InterruptedException e) {
//			throw new StockAppBizException("系统清仓失败");
//		} catch (ExecutionException e) {
//			Throwable t = e.getCause();
//            if( t instanceof CommandConstraintViolatedException){
//                throw (CommandConstraintViolatedException)t;
//            }else if(t instanceof StockAppBizException){
//            	throw (StockAppBizException)t;
//            }else{
//            	throw new StockAppBizException("系统清仓失败");
//            }
//		}
	}
	@Override
	public TradingAccountListBean getMyAllTradingAccountList(final int start,
			final int limit) {
		Async<GainVOs> vos = getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class)
				.getTotalGain(start, limit);
		AsyncFuture<TradingAccountListBean> future = AsyncFuture.newAsyncFuture(vos, new IDataConverter<GainVOs, TradingAccountListBean>() {

			@Override
			public TradingAccountListBean convert(GainVOs value) {
				List<GainVO> volist =value==null?null:value.getGains() ;
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
		});
		throw new ExecAsyncException(future);

//		List<GainVO> volist = null;
//		try {
//			volist = fetchDataFromServer(new Callable<List<GainVO>>() {
//				public List<GainVO> call() throws Exception {
//					try {
//						if (log.isDebugEnabled()) {
//							log.debug("fetch all trading account info...");
//						}
//						GainVOs vos=getRestService(ITradingProtectedResource.class)
//								.getTotalGain(start, limit);
//						List<GainVO> list =vos==null?null:vos.getGains() ;
//						return list;
//					} catch (Throwable e) {
//						log.warn("Failed to fetch all trading account", e);
//						throw new StockAppBizException(e.getMessage());
//					}
//				}
//			});
//		} catch (Exception e) {
//			log.warn("Failed to fetch all trading account", e);
//		}
//		if (volist != null && volist.size() > 0) {
//			List<GainBean> beanList = new ArrayList<GainBean>();
//			for (GainVO vo : volist) {
//				GainBean bean = ConverterUtils.fromVO(vo);
//				beanList.add(bean);
//			}
//			myTradingAccounts.setAllTradingAccounts(beanList);
//		}
//		return myTradingAccounts;
	}
	
	private <T> T getRestService(Class<T> ifproxy,Class<?> ifRest) {
		return context.getService(IRestProxyService.class).getRestService(
				ifproxy,ifRest);
	}


	@Override
	public TradingAccountListBean getMySuccessTradingAccountList(
			final int start, final int limit) {
		Async<GainVOs> vos = getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class)
				.getTotalGain(start, limit);
		AsyncFuture<TradingAccountListBean> future = AsyncFuture.newAsyncFuture(vos, new IDataConverter<GainVOs, TradingAccountListBean>() {

			@Override
			public TradingAccountListBean convert(GainVOs value) {
				List<GainVO> volist =value==null?null:value.getGains() ;
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
		});
		throw new ExecAsyncException(future);
//		List<GainVO> volist = null;
//		try {
//			volist = fetchDataFromServer(new Callable<List<GainVO>>() {
//				public List<GainVO> call() throws Exception {
//					try {
//						if (log.isDebugEnabled()) {
//							log.debug("fetch all trading account info...");
//						}
//						GainVOs vos=getRestService(ITradingProtectedResource.class)
//								.getTotalGain(start, limit);
//						List<GainVO> list =vos==null?null:vos.getGains() ;
//						return list;
//					} catch (Throwable e) {
//						log.warn("Failed to fetch success trading account", e);
//						throw new StockAppBizException(e.getMessage());
//					}
//				}
//			});
//		} catch (Exception e) {
//			log.warn("Failed to fetch success trading account", e);
//		}
//		if (volist != null && volist.size() > 0) {
//			List<GainBean> beanList = new ArrayList<GainBean>();
//			List<GainBean> vbeanList = new ArrayList<GainBean>();
//			List<GainBean> rbeanList = new ArrayList<GainBean>();
//			for (GainVO vo : volist) {
//				GainBean bean = ConverterUtils.fromVO(vo);
//				beanList.add(bean);
//				if (vo.isVirtual()) {
//					vbeanList.add(bean);
//				}else{
//					rbeanList.add(bean);
//				}
//			}
//			myTradingAccounts.setSuccessTradingAccounts(beanList);
//		}
//		return myTradingAccounts;
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
         tradingRecordBean_cache.doReload(true,p,null);
         tradingRecordBean_cache.setCommandParameters(p);
	    return tradingRecordBeans;
	}
	class TradingRecordBeanComparator implements Comparator<TradingRecordBean>{
        @Override
        public int compare(TradingRecordBean b1, TradingRecordBean b2) {
          return b1.getDate()>b2.getDate()?-1:1;
        }
    }
//	// =================private method =======================================
//	private <T> T fetchDataFromServer(Callable<T> task) throws Exception{
//		AsyncUtils.execCallableInAsync(context.getService(ICommandExecutor.class), task);
//		Future<T> future = context.getExecutor().submit(task);
//		T result = null;
//		try {
//			result = future.get();
//			return result;
//		} catch (Exception e) {
//			log.warn("Error when fetching data from server", e);
//			throw e;
//		}
//		return null;
//	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadTMegagameRank(boolean)
	 */
	@Override
	public void reloadTMegagameRank(boolean wait4Finish) {
		if(wait4Finish){
			AsyncUtils.forceLoadAsyncInUI(tRankCache, null);
		}else{
			tRankCache.doReload(false,null,null);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadT1MegagameRank(boolean)
	 */
	@Override
	public void reloadT1MegagameRank(boolean wait4Finish) {
		if(wait4Finish){
			AsyncUtils.forceLoadAsyncInUI(t1RankCache, null);
		}else{
			t1RankCache.doReload(false,null,null);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadRegularTicketRank(boolean)
	 */
	@Override
	public void reloadRegularTicketRank(boolean wait4Finish) {
		if(wait4Finish){
			AsyncUtils.forceLoadAsyncInUI(rtRankCache, null);
		}else{
			rtRankCache.doReload(false,null,null);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#reloadWeekRank(boolean)
	 */
	@Override
	public void reloadWeekRank(boolean wait4Finish) {
		if(wait4Finish){
			AsyncUtils.forceLoadAsyncInUI(weekRankCache, null);
		}else{
			weekRankCache.doReload(false,null,null);
		}
	}

	@Override
	public void reloadEarnRank(int start, int limit, boolean wait4Finish) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", start);
		map.put("limit", limit);
		if(wait4Finish){
			AsyncUtils.forceLoadAsyncInUI(earnRankCache, map);
		}else{
			weekRankCache.doReload(true,map,null);
		}
	}

	

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getGain(int, int)
	 */
	@Override
	public BindableListWrapper<GainBean> getTotalGain(int start, int limit) {
		return getTotalGain(start, limit, false);
	}
	@Override
	public BindableListWrapper<GainBean> getTotalGain(int start, int limit, boolean wait4Finish) {
		if(rightTotalGain==null){
				this.rightTotalGain = getRightTotalGainCache().getEntities(null, new Comparator<GainBean>(){
					@Override
					public int compare(GainBean o1, GainBean o2) {
						return o2.getCloseTime().compareTo(o1.getCloseTime());
					}

				});
		}
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("start", start);
		params.put("limit", limit);
		if (wait4Finish) {
			return AsyncUtils.forceLoadNFetchAsyncInUI(getRightTotalGainCache(),params, new AsyncFuture<BindableListWrapper<GainBean>>(),
        			new IEntityFetcher<BindableListWrapper<GainBean>>() {

						@Override
						public BindableListWrapper<GainBean> fetchFromCache(
								IBindableEntityCache<?, ?> cache) {
							return rightTotalGain;
						}
					});
		}else{
			getRightTotalGainCache().doReload(false,params,null);
		}
		//rightTotalGainCacheDoReload(start, limit, wait4Finish);
		return this.rightTotalGain;

	}

	protected void rightTotalGainCacheDoReload(int start, int limit,
			boolean wait4Finish) {
		Map<String, Object> commandParameters = new HashMap<String, Object>();
		commandParameters.put("start", start);
		commandParameters.put("limit", limit);
		if (wait4Finish) {
			AsyncUtils.forceLoadNFetchAsyncInUI(getLeftSuccessGainCache(),
					commandParameters,new AsyncFuture<BindableListWrapper<GainBean>>(),new IEntityFetcher<BindableListWrapper<GainBean>>() {

						@Override
						public BindableListWrapper<GainBean> fetchFromCache(
								IBindableEntityCache<?, ?> cache) {
							return rightGain;
						}
					});
		} else {
			getLeftSuccessGainCache().doReload(false, commandParameters, null);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getGain(int, int)
	 */
	@Override
	public BindableListWrapper<GainBean> getGain(int start, int limit) {
		return getGain(start, limit, false);
	}
	@Override
	public BindableListWrapper<GainBean> getGain(int start, int limit, boolean wait4Finish) {
		if(rightGain==null){
			this.rightGain =getLeftSuccessGainCache().getEntities(new IEntityFilter<GainBean>() {
				
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
		rightTotalGainCacheDoReload(start, limit, wait4Finish);
		return this.rightGain;
	}

	protected GenericReloadableEntityCache<String, GainBean, GainVO> getRightTotalGainCache() {
		if(rightTotalGainCache==null){
			rightTotalGainCache=new GenericReloadableEntityCache<String, GainBean, GainVO>("rightTotalGain"){
				@Override
				protected Map<String, Object> prepareLoadmoreCommandParameter(
						BindableListWrapper<GainBean> list) {
					Map<String, Object> params=new HashMap<String, Object>();
					int start = rightTotalGainCache.getCacheSize();
					params.put("start", start);
					params.put("limit", 20);
					return params;
				}

			};
		}
		return rightTotalGainCache;
	}
	
	protected GenericReloadableEntityCache<String, GainBean, GainVO> getLeftSuccessGainCache() {
		if(leftSuccessGainCache==null){
			leftSuccessGainCache=new GenericReloadableEntityCache<String, GainBean, GainVO>("leftSuccessGain"){
				@Override
				protected Map<String, Object> prepareLoadmoreCommandParameter(
						BindableListWrapper<GainBean> list) {
					Map<String, Object> params=new HashMap<String, Object>();
					int start = leftSuccessGainCache.getCacheSize();
					params.put("start", start);
					params.put("limit", 20);
					return params;
				}

			};
		}
		return leftSuccessGainCache;
	}
	BindableListWrapper<TradingAccInfoBean> allT;
	
	//获取我的T日 和 T+1日交易盘
	public BindableListWrapper<TradingAccInfoBean> getAllTradingAccountList(){
		tradingAccInfo_cache.doReload(false,null,null);
    	if(allT == null)
    		allT = tradingAccInfo_cache.getEntities(null, new  TradingAccInfoBeanComparator());
		AsyncUtils.forceLoadNFetchAsyncInUI(this.tradingAccInfo_cache, null, new AsyncFuture<BindableListWrapper<TradingAccInfoBean>>(), new IEntityFetcher<BindableListWrapper<TradingAccInfoBean>>() {

			public BindableListWrapper fetchFromCache(IBindableEntityCache cache) {
				return allT;
			}
		});
		return this.allT;
	}
	
	public BindableListWrapper<TradingAccInfoBean> getSyncAllTradingAccountList(){
		return AsyncUtils.forceLoadNFetchAsyncInUI(tradingAccInfo_cache, null, new AsyncFuture<BindableListWrapper<TradingAccInfoBean>>(), new IEntityFetcher<BindableListWrapper<TradingAccInfoBean>>() {

			@Override
			public BindableListWrapper<TradingAccInfoBean> fetchFromCache(
					IBindableEntityCache<?, ?> cache) {
				return tradingAccInfo_cache.getEntities(null, new  TradingAccInfoBeanComparator());
			}
		});
	}
	
    //获取我的T日交易盘
    public BindableListWrapper<TradingAccInfoBean> getT0TradingAccountList(){
        tradingAccInfo_cache.doReload(false,null,null);
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
        tradingAccInfo_cache.doReload(false,null,null);
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
        this.userCreateTradAccInfo_Cache.doReload(true,null,null);
        return this.userCreateTradAccInfo;
    }

    @Override
    public void createTradingAccount(Long captitalAmount, float capitalRate, boolean virtual, float depositRate,String assetType) throws StockAppBizException {
		doCreateTradingAccount(new CreateTradingAccountCommand(captitalAmount,  capitalRate,  virtual,  depositRate,assetType));
    	
//		try {
//			Future<StockResultVO> f = context.getService(ICommandExecutor.class).submitCommand(new CreateTradingAccountCommand(captitalAmount,  capitalRate,  virtual,  depositRate,assetType));
//			Object result = f.get();
//			if (result != null && result instanceof StockResultVO) {
//	            StockResultVO vo=(StockResultVO) result;
//	           
//	                if (vo.getSuccOrNot() == 0) {
//	                    if (log.isDebugEnabled()) {
//	                        log.debug("Failed to create trading account, caused by "
//	                                + vo.getCause());
//	                    }
//	                    throw new StockAppBizException(vo.getCause());
//	                }
//	                if (vo.getSuccOrNot() == 1) {
//	                    if (log.isDebugEnabled()) {
//	                        log.debug("Create trading account successfully.");
//	                    }
//	                    tradingAccInfo_cache.forceReload(true);
//	                }
//	           }			
//		}catch (ExecutionException e) {
//			Throwable t = e.getCause();
//            if( t instanceof CommandConstraintViolatedException){
//                throw (CommandConstraintViolatedException)t;
//            }else if(t instanceof StockAppBizException){
//            	throw (StockAppBizException)t;
//            }else{
//                throw new StockAppBizException("创建交易盘失败");
//            }
//		} catch (InterruptedException e){
//			throw new StockAppBizException("创建交易盘失败");
//		}
    }

	/**
	 * @param captitalAmount
	 * @param capitalRate
	 * @param virtual
	 * @param depositRate
	 * @param assetType
	 */
	public void doCreateTradingAccount(CreateTradingAccountCommand command) {
		AsyncUtils.execCommandAsyncInUI(command,
				new IDataConverter<StockResultVO, Object>() {

					@Override
					public Object convert(StockResultVO vo)
							throws NestedRuntimeException {
		                if (vo.getSuccOrNot() == 0) {
	                    if (log.isDebugEnabled()) {
	                        log.debug("Failed to create trading account, caused by "
	                                + vo.getCause());
	                    }
	                    throw new NestedRuntimeException(new StockAppBizException(vo.getCause()));
	                }
	                if (vo.getSuccOrNot() == 1) {
	                    if (log.isDebugEnabled()) {
	                        log.debug("Create trading account successfully.");
	                    }
						try {//交易盘创建成功后及时更新首页
							refreshHomePage();
						} catch (Exception e) {
							log.warn("Refresh home page after create trading account", e);
						}
					}
	                return null;
					}
		});
	}
    
    @Override
    public void buyStock(final String acctID, final String market, final String code, final String price, final String amount) throws StockAppBizException {
		AsyncFuture<StockResultVO> future = new AsyncFuture<StockResultVO>(){

			/* (non-Javadoc)
			 * @see com.wxxr.mobile.core.async.api.AsyncFuture#getInternalCallback()
			 */
			@Override
			public IAsyncCallback<StockResultVO> getInternalCallback() {
				return new DelegateCallback<StockResultVO, StockResultVO>(super.getInternalCallback()) {

					@Override
					protected StockResultVO getTargetValue(StockResultVO value) {
						return value;
					}

					/* (non-Javadoc)
					 * @see com.wxxr.mobile.core.async.api.DelegateCallback#success(java.lang.Object)
					 */
					@Override
					public void success(StockResultVO result) {
	                    if (log.isDebugEnabled()) {
	                        log.debug("clearTradingAccount successfully.");
	                    }
	                    Map<String, Object> params=new HashMap<String, Object>(); 
	                    params.put("acctID", acctID);
						AsyncUtils.forceLoadInSync(tradingAccountBean_cache, params);
						super.success(result);
					}
				};
			}
			
		};
		context.getService(ICommandExecutor.class).submitCommand(new BuyStockCommand( acctID,  market,  code,  price,  amount),future.getInternalCallback());
		throw new ExecAsyncException(future);
//        try {
//        	Future<StockResultVO> f=  context.getService(ICommandExecutor.class).submitCommand(new BuyStockCommand( acctID,  market,  code,  price,  amount));
//        	StockResultVO result= f.get();
//           if (result != null ) {
//               StockResultVO vo=result;
//                   if (vo.getSuccOrNot() == 0) {
//                       if (log.isDebugEnabled()) {
//                           log.debug("Failed to buyStock, caused by "
//                                   + vo.getCause());
//                       }
//                       throw new StockAppBizException(vo.getCause());
//                   }
//                   if (vo.getSuccOrNot() == 1) {
//                       if (log.isDebugEnabled()) {
//                           log.debug("buyStock successfully.");
//                       }
//                       tradingAccountBean_cache.forceReload(true);
//                   }
//               }
//        }catch (ExecutionException e) {
//			Throwable t = e.getCause();
//            if( t instanceof CommandConstraintViolatedException){
//                throw (CommandConstraintViolatedException)t;
//            }else if(t instanceof StockAppBizException){
//            	throw (StockAppBizException)t;
//            }else{
//                throw new StockAppBizException("买人股票失败");
//            }
//		} catch (InterruptedException e){
//			throw new StockAppBizException("买人股票失败");
//		}      
    }

    @Override
    public void sellStock(String acctID, String market, String code, String price, String amount) throws StockAppBizException {
    	AsyncUtils.execCommandAsyncInUI(context.getService(ICommandExecutor.class), new SellStockCommand( acctID,  market,  code,  price,  amount));
    }
    @Override
    public void quickBuy(Long captitalAmount, String capitalRate, boolean virtual, String stockMarket, String stockCode, String stockBuyAmount, String depositRate,String assetType ) throws StockAppBizException {
    	AsyncUtils.execCommandAsyncInUI(context.getService(ICommandExecutor.class), new QuickBuyStockCommand( captitalAmount,  capitalRate,  virtual,  stockMarket,  stockCode,  stockBuyAmount,  depositRate,assetType));
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
        this.tradingAccountBean_cache.setCommandParameters(params);
        this.tradingAccountBean_cache.doReload(true,params,null);
        return tradingAccountBean_cache.getEntity(Long.valueOf(acctID));
    }

  //未结算
    @Override
    public TradingAccountBean getSyncTradingAccountInfo(final String acctID) throws StockAppBizException {
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
        return AsyncUtils.forceLoadNFetchAsyncInUI(this.tradingAccountBean_cache, params, new AsyncFuture<TradingAccountBean>(), 
        		new IEntityFetcher<TradingAccountBean>() {

					@Override
					public TradingAccountBean fetchFromCache(
							IBindableEntityCache<?, ?> cache) {
				        return tradingAccountBean_cache.getEntity(Long.valueOf(acctID));
					}
		});
    }
    
    @Override
    public void cancelOrder(final String accId,final String orderID) {
    	AsyncUtils.execCommandAsyncInUI(new CancelOrderCommand(orderID), new IDataConverter<StockResultVO, Object>() {

    		@Override
    		public Object convert(StockResultVO result) {
    			if (result.getSuccOrNot() == 0) {
    				if (log.isDebugEnabled()) {
    					log.debug("Failed to cancelOrder, caused by "
    							+ result.getCause());
    				}
    				throw new NestedRuntimeException(new StockAppBizException(result.getCause()));
    			}
    			if (result.getSuccOrNot() == 1) {
    				if (log.isDebugEnabled()) {
    					log.debug("cancelOrder successfully.");
    				}
    				try {
    					TradingAccountBean bean = tradingAccountBean_cache.getEntity(Long.valueOf(accId));
    					if (bean!=null) {
    						List<StockTradingOrderBean>  orders = bean.getTradingOrders();
    						if (orders!=null) {
    							for (StockTradingOrderBean order : orders) {
    								if (order.getId().toString().equals(orderID)) {
    									order.setStatus("100");
    									break;
    								}
    							}
    						}
    					}
    				} catch (Throwable e) {
    					log.warn(" Failed to update local cache",e);
    				}
    			}
    			return null;
    		}
    	});
//    	TradingAccountBean bean = tradingAccountBean_cache.getEntity(Long.valueOf(accId));
//		try {
//			Future<StockResultVO> f = context.getService(ICommandExecutor.class).submitCommand(new CancelOrderCommand( orderID));
//			Object result = f.get();
//	    	if (result != null && result instanceof StockResultVO) {
//	            StockResultVO vo=(StockResultVO) result;
//	           
//	                if (vo.getSuccOrNot() == 0) {
//	                    if (log.isDebugEnabled()) {
//	                        log.debug("Failed to cancelOrder, caused by "
//	                                + vo.getCause());
//	                    }
//	                    throw new StockAppBizException(vo.getCause());
//	                }
//	                if (vo.getSuccOrNot() == 1) {
//	                    if (log.isDebugEnabled()) {
//	                        log.debug("cancelOrder successfully.");
//	                    }
//	                    if (bean!=null) {
//	            			List<StockTradingOrderBean>  orders = bean.getTradingOrders();
//	            			if (orders!=null) {
//	            				for (StockTradingOrderBean order : orders) {
//	            					if (order.getId().toString().equals(orderID)) {
//	            						order.setStatus("100");
//	            						break;
//	            					}
//	            				}
//	            			}
//	            		}
//	                }
//	            }			
//		} catch (InterruptedException e) {
//			throw new StockAppBizException("系统执行撤单失败");
//		} catch (ExecutionException e) {
//			Throwable t = e.getCause();
//            if( t instanceof CommandConstraintViolatedException){
//                throw (CommandConstraintViolatedException)t;
//            }else if(t instanceof StockAppBizException){
//            	throw (StockAppBizException)t;
//            }else{
//            	throw new StockAppBizException("系统执行撤单失败");
//            }
//		}
    }

	@Override
	public void applyDrawMoney(long amount) {
		ApplyDrawMoneyCommand cmd=new ApplyDrawMoneyCommand();
		cmd.setAmount(amount);
    	AsyncUtils.execCommandAsyncInUI(context.getService(ICommandExecutor.class), cmd);
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
        this.voucherDetailsBean_cache.doReload(true,params, null);
        return voucherDetailsBean_cache.getEntity(key);
	}
	private static final Comparator<GainPayDetailBean> vgainPayDetailsComparator = new Comparator<GainPayDetailBean>() {
		
		@Override
		public int compare(GainPayDetailBean o1, GainPayDetailBean o2) {
			if(o1!=null && o2!=null){
				return o2.getTime() - o1.getTime() > 0 ? 1:-1;
			}
			return 0;
		}
	};
	public BindableListWrapper<GainPayDetailBean> getGainPayDetailDetails(int start, int limit){
		return getGainPayDetailDetails(start,limit,true);
	}
	
	public BindableListWrapper<GainPayDetailBean> getGainPayDetailDetails(int start, int limit, boolean wait4Finish){
		if (log.isDebugEnabled()) {
			log.debug(String.format("params:[start=%s,limit=%s]", start,limit));
		}
		if(this.vgainPayDetails == null){
			if(this.gainPayDetailBean_cache == null){
				this.gainPayDetailBean_cache = new  GenericReloadableEntityCache<String,GainPayDetailBean,List<GainPayDetailBean>>("vgainPayDetails"){

					/* (non-Javadoc)
					 * @see com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache#prepareLoadmoreCommandParameter(java.util.Map)
					 */
					@Override
					protected Map<String, Object> prepareLoadmoreCommandParameter(
							BindableListWrapper<GainPayDetailBean> list) {
						Map<String, Object> params=new HashMap<String, Object>();
						List<GainPayDetailBean> data = list.getData();
						int start = data != null ? data.size() : 0;
						params.put("start", start);
						params.put("limit", 20);
						return params;
					}

				};
			}
			this.vgainPayDetails = this.gainPayDetailBean_cache.getEntities(null, vgainPayDetailsComparator);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		if(wait4Finish){
			return AsyncUtils.forceLoadNFetchAsyncInUI(this.gainPayDetailBean_cache, params, 
					new AsyncFuture<BindableListWrapper<GainPayDetailBean>>(), new IEntityFetcher<BindableListWrapper<GainPayDetailBean>>() {

						@Override
						public BindableListWrapper<GainPayDetailBean> fetchFromCache(
								IBindableEntityCache<?, ?> cache) {
							return vgainPayDetails;
						}
					});
		}else{
			this.gainPayDetailBean_cache.doReload(true,params,null);
		}
		return this.vgainPayDetails;
	}
	
	void clearCache(){
		if (this.menu!=null) {
			menu.setMenuItems(null);
		}
		if (tradingAccInfo_cache!=null) {
			tradingAccInfo_cache.clear();
		}
		if (tradingAccountBean_cache!=null) {
			tradingAccountBean_cache.clear();
		}
        if (tradingRecordBean_cache!=null) {
        	tradingRecordBean_cache.clear();
		}
        if (dealDetailBean_cache!=null) {
        	dealDetailBean_cache.clear();
		}
        if (auditDetailBean_cache!=null) {
        	auditDetailBean_cache.clear();
		}
        if (voucherDetailsBean_cache!=null) {
        	 voucherDetailsBean_cache.clear();
		}
        if (gainPayDetailBean_cache!=null) {
        	gainPayDetailBean_cache.clear();
		}
       
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
	    addRequiredService(IMessageManagementService.class);
	}

	@Override
	public void destroy(Object serviceHandler) {
		clearCache();
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
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getHomeMenuList()
						 */
						public HomePageMenu getHomeMenuList() {
							return ((ITradingManagementService)holder.getDelegate()).getHomeMenuList();
						}
						/**
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getHomeMenuList()
						 */
						public HomePageMenu getHomeMenuList(boolean forceload) {
							return ((ITradingManagementService)holder.getDelegate()).getHomeMenuList(forceload);
						}
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
						public void cancelOrder(String accId,String orderID) {
							((ITradingManagementService)holder.getDelegate()).cancelOrder(accId,orderID);
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
						 * @param start
						 * @param limit
						 * @param wait4Finish
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getGain(int, int)
						 */
						public BindableListWrapper<GainBean> getGain(int start,
								int limit, boolean wait4Finish) {
							return ((ITradingManagementService)holder.getDelegate()).getGain(start, limit, wait4Finish);
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

						/**
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getAllTradingAccountList()
						 */
						public BindableListWrapper<TradingAccInfoBean> getAllTradingAccountList() {
							return ((ITradingManagementService)holder.getDelegate()).getAllTradingAccountList();
						}

						/**
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getSyncAllTradingAccountList()
						 */
						public BindableListWrapper<TradingAccInfoBean> getSyncAllTradingAccountList() {
							return ((ITradingManagementService)holder.getDelegate()).getSyncAllTradingAccountList();
						}

						/**
						 * @param acctID
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getSyncTradingAccountInfo(String)
						 */
						public TradingAccountBean getSyncTradingAccountInfo(
								String acctID) throws StockAppBizException {
							return ((ITradingManagementService)holder.getDelegate()).getSyncTradingAccountInfo(acctID);
						}

						/**
						 * @param start
						 * @param limit
						 * @param wait4Finish
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getTotalGain(int, int, boolean)
						 */
						public BindableListWrapper<GainBean> getTotalGain(
								int start, int limit, boolean wait4Finish) {
							return ((ITradingManagementService)holder.getDelegate()).getTotalGain(start, limit, wait4Finish);
						}
						@Override
						public BindableListWrapper<DrawMoneyRecordBean> getDrawMoneyRecordList(
								int start, int limit, boolean wait4Finish) {
							return ((ITradingManagementService)holder.getDelegate()).getDrawMoneyRecordList(start, limit, wait4Finish);
						}
						@Override
						public void createTradingAccount(Long captitalAmount,
								float capitalRate, boolean virtual,
								float depositRate, String assetType,
								String tradingType) {
							 ((ITradingManagementService)holder.getDelegate()).createTradingAccount(captitalAmount, capitalRate, virtual, depositRate, assetType, tradingType);
							
						}
						@Override
						public TradingConfigBean getTradingConfig(
								String tradingType, boolean isVirtual) {
							return ((ITradingManagementService)holder.getDelegate()).getTradingConfig(tradingType, isVirtual);
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
			clearCache();
			impl.doInit();
			return impl;
		}catch (CloneNotSupportedException e) {
			throw new RuntimeException("SHOULD NOT HAPPEN !");
		}
	}
	
	
	/*****************V2********************/

	private HomePageMenu menu;
	
	public HomePageMenu getHomeMenuList() {
		return getHomeMenuList(false);
	}
	
	public HomePageMenu getHomeMenuList(boolean forceReload) {
		if (menu==null) {
			menu = new HomePageMenu();
			forceReload = true;
		}
		if(forceReload){
			AsyncUtils.execCallableAsyncInUI(new IAsyncCallable<Object>() {

				@Override
				public void call(IAsyncCallback<Object> cb) {
					try {
						refreshHomePage();
						cb.success(null);
					}catch(Throwable t){
						cb.failed(t);
					}
				}
			}, new IDataConverter<Object, HomePageMenu>(){

				/* (non-Javadoc)
				 * @see com.wxxr.mobile.core.async.api.IDataConverter#convert(java.lang.Object)
				 */
				@Override
				public HomePageMenu convert(Object arg0)
						throws NestedRuntimeException {
					return menu;
				}
				
			});

		}
		return menu;
	}
	
	private void refreshHomePage() throws Exception {
		List<BaseMenuItem> homePageItems = new ArrayList<BaseMenuItem>();
		boolean login = getService(IUserIdentityManager.class).isUserAuthenticated();
		List<TradingAccountMenuItem> items1 = null;
		SignInMessageMenuItem sign =null;
		ChampionShipMessageMenuItem champion = null;
		try {
			Async<AppHomePageListVO> async = context.getService(IRestProxyService.class).getRestService(ITradingResourceAsync.class,ITradingResource.class).unSecurityAppHome(0, 20);
			AppHomePageListVO vo = new AsyncFuture<AppHomePageListVO>(async).get();
			//参数排行榜
			MegagameRankNUpdateTimeVO m = vo.getRankVo();			
			if (m!=null) {
				champion = new ChampionShipMessageMenuItem();
				champion.setType("70");
				champion.setTitle("参赛排行榜");
				champion.setDate(sdf.format(new Date(m.getUpdateTime())));
				List<MegagameRankVO> rankl = m.getRankList();
				if (rankl!=null&&rankl.size()>0) {
					MegagameRankVO first = rankl.get(0);
					champion.setNickName(String.format("NO.1 %s", first.getNickName()));
					String stockName = null;
					StockBaseInfo stock = KUtils.getService(IStockInfoSyncService.class).getStockBaseInfoByCode(first.getMaxStockCode(), first.getMaxStockMarket());
			        if (stock!=null) {
			           stockName = stock.getName();
			        }	
					champion.setStockName(stockName);
					champion.setIncomeRate(Long.valueOf(first.getGainRates()));
				}
			}
			
			//操盘咨询
			List<PullMessageVO> pMsg = vo.getPullMessageList();
			savePullMessages(pMsg);
			if (login) {
				Async<SecurityAppHomePageVO> voasync = context.getService(IRestProxyService.class).getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).securityAppHome();
				SecurityAppHomePageVO svo = new AsyncFuture<SecurityAppHomePageVO>(voasync).get();
				//签到信息
				UserSignVO signVo = svo.getSignMessage();
				
				if (signVo!=null) {
					sign = new SignInMessageMenuItem();
					sign.setHasSignIn(signVo.getSign());
					sign.setType("80");
					sign.setTitle("每日签到");
					sign.setDate(StringUtils.isBlank(signVo.getSignDate())?sdf1.format(new Date())+" 00:00":sdf.format(new Date(Long.valueOf(signVo.getSignDate()))));
					if (signVo.getSign()) {
						sign.setMessage("今日已签到");
					}else{
						sign.setMessage(String.format("可获%d实盘积分", signVo.getRewardVol()));
					}
					sign.setScore((int)signVo.getRewardVol());
					sign.setSignDays((int)signVo.getOngoingDays());
				}
				//系统消息
				List<MessageVO> sMsg = svo.getRemindMessage();
				saveRemindMessages(sMsg);
				items1 = getTradingAccountMenuItem(svo.getTradingAcctList());
			}
			
		} catch (Exception e) {
			log.warn("Error when get menu info", e);
			throw e;
		}
		
		
		if (items1!=null&&items1.size()>0) {
			homePageItems.addAll(items1);
		}
		//assemble menu
		if (sign!=null&&!sign.isHasSignIn()) {//未签到
			homePageItems.add(sign);
		}
		MessageMenuItem msg_item = generateMsgMenuItem1();
		if (msg_item!=null) {//操盘咨询
			homePageItems.add(msg_item);
		}
		if (champion!=null) {//参赛交易盘
			homePageItems.add(champion);
		}
		if (login) {
			msg_item = generateMsgMenuItem2();
			if (msg_item!=null) {//系统消息
				homePageItems.add(msg_item);
			}
		}
		
		if (sign!=null&&sign.isHasSignIn()) {//已签到
			homePageItems.add(sign);
		}
		
		if (menu==null) {
			menu = new HomePageMenu();
		}
		menu.setMenuItems(homePageItems);
	}
	
	
	private void savePullMessages(List<PullMessageVO> pMsgs) {
		if (pMsgs!=null&&pMsgs.size()>0) {
			for (PullMessageVO vo : pMsgs) {
				PullMessageBean bean=new PullMessageBean();
				bean.setPullId(vo.getId());
				bean.setArticleUrl(vo.getArticleUrl());
				bean.setMessage(vo.getMessage());
				bean.setPhone(vo.getPhone());
				bean.setRead(false);
				bean.setTitle(vo.getTitle());
				bean.setCreateDate(vo.getCreateDate());
				getService(IMessageManagementService.class).savePullMsg(bean);
			}
		}
		
	}

	private void saveRemindMessages(List<MessageVO> sMsgs) {
		if (sMsgs!=null&&sMsgs.size()>0) {
			for (MessageVO vo : sMsgs) {
				RemindMessageBean bean=new RemindMessageBean();
				bean.setId(vo.getId());
				bean.setAcctId(vo.getId());
				bean.setAttrs(vo.getAttributes());
				bean.setContent(vo.getContent());
				bean.setCreatedDate(vo.getCreatedDate());
				bean.setTitle(vo.getAttributes().get("title"));
				bean.setType(bean.getType());
				getService(IMessageManagementService.class).saveRemindMsg(bean);
			}
		}
		
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
	private SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日");
	
	private MessageMenuItem generateMsgMenuItem1(){//生成操盘咨询菜单项
		List<PullMessageBean> list = getService(IMessageManagementService.class).getUnReadPullMessage();
		MessageMenuItem msg_item = new MessageMenuItem();
		msg_item.setType("61");
		msg_item.setTitle("操盘资讯");
		PullMessageBean msg =  getService(IMessageManagementService.class).getFirstPullMessage();
		if (msg!=null) {
			msg_item.setDate(sdf.format(new Date(Long.valueOf(msg.getCreateDate()))));
			msg_item.setMessage(msg.getMessage());
		}
		int size = 0;
		if (list!=null) {
			size = list.size()>20?20:list.size();
		}
		msg_item.setNum(size);
		return msg_item; 
	}
	private MessageMenuItem generateMsgMenuItem2(){//生成系统消息菜单项
		List<RemindMessageBean> list = getService(IMessageManagementService.class).getUnReadRemindMessage();
		MessageMenuItem msg_item = new MessageMenuItem();
		msg_item.setType("60");
		msg_item.setTitle("系统消息");
		RemindMessageBean msg = getService(IMessageManagementService.class).getFirstRemindMessage();
		if (msg!=null) {
			String date = null;
			String time = null;
			if (msg.getAttrs()!=null) {
				date = msg.getAttrs().get("ms");
				time = msg.getAttrs().get("time");
			}
			msg_item.setDate(StringUtils.isBlank(date)?sdf1.format(new Date())+" "+time:sdf.format(new Date(Long.valueOf(date))));
			msg_item.setMessage(msg.getTitle());
		}
		msg_item.setNum(list==null?0:list.size());
		return msg_item;
	}
	private Comparator<TradingAccountMenuItem> c = new Comparator<TradingAccountMenuItem>() {

		@Override
		public int compare(TradingAccountMenuItem o1, TradingAccountMenuItem o2) {
			int ret = o1.getStatus().compareTo(o2.getStatus());
			if (ret==0) {
				ret = o2.getType().substring(0, 1).compareTo(o1.getType().substring(0, 1));
			}
			if (ret==0) {
				ret = o2.getDate().compareTo(o1.getDate());
			}
			return ret;
		}
	};
			
	private List<TradingAccountMenuItem> getTradingAccountMenuItem(List<TradingAccInfoVO> volist) {
		List<TradingAccountMenuItem> tradingItemList = null;
		SimpleDateFormat df = new SimpleDateFormat("MM月dd日");
		if (volist!=null&&volist.size()>0) {
			tradingItemList = new ArrayList<TradingAccountMenuItem>();
			for (TradingAccInfoVO vo : volist) {
				TradingAccountMenuItem trading = new TradingAccountMenuItem();
				trading.setAcctId(vo.getAcctID()+"");
				trading.setDate(df.format(new Date(vo.getCreateDate())));
				String stockName = vo.getMaxStockName();
				if (StringUtils.isBlank(stockName)) {
		           StockBaseInfo stock = KUtils.getService(IStockInfoSyncService.class).getStockBaseInfoByCode(vo.getMaxStockCode(), vo.getMaxStockMarket());
		           if (stock!=null) {
		               stockName = stock.getName();
		           }
				}
				trading.setMaxHoldStockName(stockName);
				trading.setIncome(vo.getTotalGain());
				trading.setIncomeRate(vo.getTotalGain()*100.0f/vo.getSum()*1.00f);
				trading.setStatus(getStockStatus(vo));
				if (vo.isVirtual()) {
					trading.setType("0");
					trading.setTitle("参赛模拟盘");
				}else if ("ASTOCKT1".equals(vo.getAcctType())) {
					trading.setType("11");
					trading.setTitle("挑战交易盘 ");
				}else if("ASTOCKT3".equals(vo.getAcctType())){
					trading.setType("13");
					trading.setTitle("挑战交易盘 ");
				}else if("ASTOCKTN".equals(vo.getAcctType())){
					trading.setType("1d");
					trading.setTitle("挑战交易盘");
				}
				tradingItemList.add(trading);
			}
			Collections.sort(tradingItemList, c);
		}
		return tradingItemList;
	}
	private String getStockStatus(TradingAccInfoVO vo){
		if ("CLOSED".equals(vo.getOver())) {
			return "2";//已结算
		}else if (vo.getStatus()==0) {
			return "1";//可卖
		}else if(vo.getStatus()==1){
			return "0";//可买
		}
		return "9";
	}
	private BindableListWrapper<DrawMoneyRecordBean> drawMoneyRecords;
	
	private Comparator<DrawMoneyRecordBean> dcomparator=  new Comparator<DrawMoneyRecordBean>() {

		@Override
		public int compare(DrawMoneyRecordBean o1, DrawMoneyRecordBean o2) {
			if (StringUtils.isNotBlank(o1.getDrawDate())&&StringUtils.isNotBlank(o2.getDrawDate())) {
				return o2.getDrawDate().compareTo(o1.getDrawDate());
			}
			return 0;
		}
	};
	@Override
	public BindableListWrapper<DrawMoneyRecordBean> getDrawMoneyRecordList(int start,
			int limit, boolean wait4Finish) {
		if (drawMoneyRecordBean_cache==null) {
			drawMoneyRecordBean_cache = new GenericReloadableEntityCache<Long, DrawMoneyRecordBean, List<DrawMoneyRecordVo>>(
					"drawMoneyRecordBean") {
				@Override
				protected Map<String, Object> prepareLoadmoreCommandParameter(
						BindableListWrapper<DrawMoneyRecordBean> list) {
					Map<String, Object> params = new HashMap<String, Object>();
					List<DrawMoneyRecordBean> data = list.getData();
					int start = data != null ? data.size() : 0;
					params.put("start", start);
					params.put("limit", 20);
					return params;
				}
			};
		}
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", start);
        params.put("limit", limit);
		if (drawMoneyRecords==null) {
			drawMoneyRecords = drawMoneyRecordBean_cache.getEntities(null, dcomparator);
		}
		drawMoneyRecordBean_cache.setCommandParameters(params);
		drawMoneyRecords.setReloadParameters(params);
		if(wait4Finish){
			return AsyncUtils.forceLoadNFetchAsyncInUI(drawMoneyRecordBean_cache, params, 
					new AsyncFuture<BindableListWrapper<DrawMoneyRecordBean>>(), new IEntityFetcher<BindableListWrapper<DrawMoneyRecordBean>>() {

						@Override
						public BindableListWrapper<DrawMoneyRecordBean> fetchFromCache(
								IBindableEntityCache<?, ?> cache) {
							return drawMoneyRecords;
						}
					});
		}else{
			drawMoneyRecordBean_cache.doReload(true, params, null);
		}
		return drawMoneyRecords;
	}

	@Override
	public void createTradingAccount(Long captitalAmount, float capitalRate,
			boolean virtual, float depositRate, String assetType,
			String tradingType) {
		CreateTradingAccountCommand cmd = new CreateTradingAccountCommand(captitalAmount,  capitalRate,  virtual,  depositRate,assetType);
		cmd.setTrdingType(tradingType);
		doCreateTradingAccount(cmd);		
	}
	
	@Override
	public TradingConfigBean getTradingConfig(final String tradingType,
			boolean isVirtual) {
		boolean forceLoad = false;
		if (tradingConfig_Cache==null) {
			tradingConfig_Cache = new GenericReloadableEntityCache<String, TradingConfigBean, TradingConfigVO>("tradingConfigBean");
		}
		TradingConfigBean bean = tradingConfig_Cache.getEntity(tradingType);
		if (bean==null) {
			bean = new TradingConfigBean();
			tradingConfig_Cache.putEntity(tradingType, bean);
			forceLoad = true;
		}
		if(forceLoad){
			return AsyncUtils.forceLoadNFetchAsyncInUI(tradingConfig_Cache, null, 
				new AsyncFuture<TradingConfigBean>(), new IEntityFetcher<TradingConfigBean>() {

					@Override
					public TradingConfigBean fetchFromCache(
							IBindableEntityCache<?, ?> cache) {
						return tradingConfig_Cache.getEntity(tradingType);
					}
				});
		}else{
			tradingConfig_Cache.doReload(false, null, null);
			return bean;
		}
	}
}

