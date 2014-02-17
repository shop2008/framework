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
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.mobile.stock.app.service.IMessageManagementService;
import com.wxxr.mobile.stock.app.service.IStockInfoSyncService;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.handler.ApplyDrawMoneyHandler;
import com.wxxr.mobile.stock.app.service.handler.ApplyDrawMoneyHandler.ApplyDrawMoneyCommand;
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
import com.wxxr.mobile.stock.app.utils.Utils;
import com.wxxr.mobile.stock.app.v2.bean.BaseMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.ChampionShipMessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.MessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.SignInMessageMenuItem;
import com.wxxr.mobile.stock.app.v2.bean.TradingAccountMenuItem;
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
import com.wxxr.stock.info.mtree.sync.bean.StockBaseInfo;
import com.wxxr.stock.notification.ejb.api.MessageVO;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingResource;
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
	private Timer homePageRefresher = new Timer("HomePage Refresh Thread");
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
        homePageRefresher.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					refreshHomePage();
				} catch (Throwable e) {
					log.warn("Error when refresh home page",e);
				}
			}
		}, 100, 10*1000);
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
		return getEarnRank(start, limit, false);
	}
	
	//赚钱榜
	public BindableListWrapper<EarnRankItemBean> getEarnRank(final int start, final int limit, boolean wait4Finish) {
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
		if(wait4Finish) {
			this.earnRankCache.forceReload(params, wait4Finish);
		} else {
			this.earnRankCache.doReloadIfNeccessay(params);			
		}
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
		return getTotalGain(start, limit, false);
	}

	public BindableListWrapper<GainBean> getTotalGain(int start, int limit, boolean wait4Finish) {
		if(rightTotalGain==null){
				this.rightTotalGain = getRightTotalGainCache().getEntities(null, new Comparator<GainBean>(){
					@Override
					public int compare(GainBean o1, GainBean o2) {
						return o2.getCloseTime().compareTo(o1.getCloseTime());
					}

				});
		}
		rightTotalGainCacheDoReload(start, limit, wait4Finish);
		return this.rightTotalGain;

	}

	protected void rightTotalGainCacheDoReload(int start, int limit, boolean wait4Finish) {
		synchronized (getRightTotalGainCache()) {
			Map<String,Object> commandParameters=new HashMap<String,Object>();
			commandParameters.put("start", start);
			commandParameters.put("limit", limit);
			if(wait4Finish) {
				getRightTotalGainCache().forceReload(commandParameters, wait4Finish);
			} else {
				getRightTotalGainCache().doReloadIfNeccessay(commandParameters);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getGain(int, int)
	 */
	@Override
	public BindableListWrapper<GainBean> getGain(int start, int limit) {
		return getGain(start, limit, false);
	}
	public BindableListWrapper<GainBean> getGain(int start, int limit, boolean wait4Finish) {
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
		rightTotalGainCacheDoReload(start, limit, wait4Finish);
		return this.rightGain;
	}

	protected GenericReloadableEntityCache<String, GainBean, GainVO> getRightTotalGainCache() {
		if(rightTotalGainCache==null){
			rightTotalGainCache=new GenericReloadableEntityCache<String, GainBean, GainVO>("rightTotalGain");
		}
		return rightTotalGainCache;
	}
	BindableListWrapper<TradingAccInfoBean> allT;
	
	//获取我的T日 和 T+1日交易盘
	public BindableListWrapper<TradingAccInfoBean> getAllTradingAccountList(){
    	if(allT == null)
    		allT = tradingAccInfo_cache.getEntities(null, new  TradingAccInfoBeanComparator());
        return allT;		
	}
	
	public BindableListWrapper<TradingAccInfoBean> getSyncAllTradingAccountList(){
		tradingAccInfo_cache.forceReload(true);
    	allT = tradingAccInfo_cache.getEntities(null, new  TradingAccInfoBeanComparator());
        return allT;		
	}
    //获取我的T日交易盘
    public BindableListWrapper<TradingAccInfoBean> getT0TradingAccountList(){
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
	                    tradingAccInfo_cache.forceReload(true);
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

  //未结算
    @Override
    public TradingAccountBean getSyncTradingAccountInfo(String acctID) throws StockAppBizException {
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
        this.tradingAccountBean_cache.forceReload(params,true);
        return tradingAccountBean_cache.getEntity(Long.valueOf(acctID));
    }
    
    @Override
    public void cancelOrder(String accId,String orderID) {
    	TradingAccountBean bean = tradingAccountBean_cache.getEntity(Long.valueOf(accId));
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
		return getGainPayDetailDetails(start, limit, false);
	}
	
	public BindableListWrapper<GainPayDetailBean> getGainPayDetailDetails(int start, int limit, boolean wait4Finish){
		if (log.isDebugEnabled()) {
			log.debug(String.format("params:[start=%s,limit=%s]", start,limit));
		}
		if(this.vgainPayDetails == null){
			if(this.gainPayDetailBean_cache == null){
				this.gainPayDetailBean_cache = new  GenericReloadableEntityCache<String,GainPayDetailBean,List<GainPayDetailBean>>("vgainPayDetails");
			}
			
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("limit", limit);
		if(wait4Finish) {
			this.gainPayDetailBean_cache.forceReload(params, wait4Finish);
		} else {
			this.gainPayDetailBean_cache.doReloadIfNeccessay(params);
		}
		this.vgainPayDetails = this.gainPayDetailBean_cache.getEntities(null, vgainPayDetailsComparator);
		return this.vgainPayDetails;
	}
	
	void clearCache(){
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
						 * @param start
						 * @param limit
						 * @param wait4Finish
						 * @return
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getGainPayDetailDetails(int, int)
						 */
						public BindableListWrapper<GainPayDetailBean> getGainPayDetailDetails(
								int start, int limit, boolean wait4Finish) {
							return ((ITradingManagementService)holder.getDelegate()).getGainPayDetailDetails(start,
									limit, wait4Finish);
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
						 * @see com.wxxr.mobile.stock.app.service.ITradingManagementService#getEarnRank(int, int, wait4Finish)
						 */
						public BindableListWrapper<EarnRankItemBean> getEarnRank(
								int start, int limit, boolean wait4Finish) {
							return ((ITradingManagementService)holder.getDelegate()).getEarnRank(start, limit, wait4Finish);
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
		if (menu==null) {
			menu = new HomePageMenu();
		}
		 return menu;
	}
	private void refreshHomePage(){
		List<BaseMenuItem> homePageItems = new ArrayList<BaseMenuItem>();
		boolean login = getService(IUserIdentityManager.class).isUserAuthenticated();
		List<TradingAccountMenuItem> items1 = null;
		SignInMessageMenuItem sign =null;
		ChampionShipMessageMenuItem champion = null;
		try {
			AppHomePageListVO vo = RestUtils.getRestService(ITradingResource.class).unSecurityAppHome(0, 4);
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
				SecurityAppHomePageVO svo = RestUtils.getRestService(ITradingProtectedResource.class).securityAppHome();
				//签到信息
				UserSignVO signVo = svo.getSignMessage();
				
				if (signVo!=null) {
					sign = new SignInMessageMenuItem();
					sign.setHasSignIn(signVo.isSign());
					sign.setType("80");
					sign.setTitle("每日签到");
					sign.setDate(signVo.getSignDate());
					if (signVo.isSign()) {
						sign.setMessage("今日已签到");
					}else{
						sign.setMessage(String.format("可获%d实盘积分", signVo.getRewardVol()));
					}
					sign.setScore((int)signVo.getRewardVol());
					sign.setSignDays(5);
				}
				//系统消息
				List<MessageVO> sMsg = svo.getRemindMessage();
				saveRemindMessages(sMsg);
				items1 = getTradingAccountMenuItem(svo.getTradingAcctList());
			}
			
		} catch (Exception e) {
			log.warn("Error when get menu info", e);
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
		msg_item = generateMsgMenuItem2();
		if (msg_item!=null) {//系统消息
			homePageItems.add(msg_item);
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
				bean.setArticleUrl(Utils.getAbsoluteURL(vo.getArticleUrl()));
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
	
	private MessageMenuItem generateMsgMenuItem1(){//生成操盘咨询菜单项
		List<PullMessageBean> list = getService(IMessageManagementService.class).getUnReadPullMessage();
		MessageMenuItem msg_item = new MessageMenuItem();
		msg_item.setType("61");
		msg_item.setTitle("操盘咨询");
		if (list!=null&&list.size()>0) {
			PullMessageBean msg = list.get(0);
			msg_item.setDate(msg.getCreateDate());
			msg_item.setMessage(msg.getMessage());
		}
		msg_item.setNum(list==null?0:list.size());
		return msg_item; 
	}
	private MessageMenuItem generateMsgMenuItem2(){//生成系统消息菜单项
		List<RemindMessageBean> list = getService(IMessageManagementService.class).getUnReadRemindMessage();
		MessageMenuItem msg_item = new MessageMenuItem();
		msg_item.setType("60");
		msg_item.setTitle("系统消息");
		if (list!=null&&list.size()>0) {
			RemindMessageBean msg = list.get(0);
			msg_item.setDate(msg.getCreatedDate());
			msg_item.setMessage(msg.getTitle());
		}
		msg_item.setNum(list==null?0:list.size());
		return msg_item;
	}
	private Comparator<TradingAccountMenuItem> c = new Comparator<TradingAccountMenuItem>() {

		@Override
		public int compare(TradingAccountMenuItem o1, TradingAccountMenuItem o2) {
			int ret = o1.getType().substring(0, 1).compareTo(o2.getType().substring(0, 1));
			if (ret==0) {
				ret = o1.getDate().compareTo(o2.getDate());
			}
			return ret;
		}
	};
			
	private List<TradingAccountMenuItem> getTradingAccountMenuItem(List<TradingAccInfoVO> volist) {
		List<TradingAccountMenuItem> tradingItemList = null;
		SimpleDateFormat df = new SimpleDateFormat("MM月DD日");
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
				trading.setIncomeRate(vo.getTotalGain()*100.0f/vo.getSum());
				trading.setStatus(getStockStatus(vo));
				if (vo.isVirtual()) {
					trading.setType("0");
					trading.setTitle("参赛模拟盘");
				}else if ("ASTCOKT1".equals(vo.getAcctType())) {
					trading.setType("11");
					trading.setTitle("挑战交易盘 T+1");
				}else if("ASTCOKT3".equals(vo.getAcctType())){
					trading.setType("13");
					trading.setTitle("挑战交易盘 T+3");
				}else if("ASTCOKTN".equals(vo.getAcctType())){
					trading.setType("1d");
					trading.setTitle("挑战交易盘 T+D");
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
		return "";
	}
	private BindableListWrapper<DrawMoneyRecordBean> drawMoneyRecords;
	
	private Comparator<DrawMoneyRecordBean> dcomparator=  new Comparator<DrawMoneyRecordBean>() {

		@Override
		public int compare(DrawMoneyRecordBean o1, DrawMoneyRecordBean o2) {
			if (StringUtils.isNotBlank(o1.getDrawDate())&&StringUtils.isNotBlank(o2.getDrawDate())) {
				return o1.getDrawDate().compareTo(o2.getDrawDate());
			}
			return 0;
		}
	};
	@Override
	public BindableListWrapper<DrawMoneyRecordBean> getDrawMoneyRecordList(int start,
			int limit, boolean wait4Finish) {
		if (drawMoneyRecordBean_cache==null) {
			drawMoneyRecordBean_cache = new GenericReloadableEntityCache<Long, DrawMoneyRecordBean, List<DrawMoneyRecordVo>>("drawMoneyRecordBean");
		}
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("start", start);
        params.put("limit", limit);
		drawMoneyRecordBean_cache.forceReload(params,wait4Finish);
		if (drawMoneyRecords==null) {
			drawMoneyRecords = drawMoneyRecordBean_cache.getEntities(null, dcomparator);
		}
		drawMoneyRecords.setReloadParameters(params);
		return drawMoneyRecords;
	}

	@Override
	public void createTradingAccount(Long captitalAmount, float capitalRate,
			boolean virtual, float depositRate, String assetType,
			String tradingType) {
		try {
			CreateTradingAccountCommand cmd = new CreateTradingAccountCommand(captitalAmount,  capitalRate,  virtual,  depositRate,assetType);
			cmd.setTrdingType(tradingType);
			Future<StockResultVO> f = context.getService(ICommandExecutor.class).submitCommand(cmd);
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
	                   refreshHomePage();
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
	public TradingConfigBean getTradingConfig(String tradingType,
			boolean isVirtual) {
		if (tradingConfig_Cache==null) {
			tradingConfig_Cache = new GenericReloadableEntityCache<String, TradingConfigBean, TradingConfigVO>("tradingConfigBean");
		}
		TradingConfigBean bean = tradingConfig_Cache.getEntity(tradingType);
		if (bean==null) {
			bean = new TradingConfigBean();
			tradingConfig_Cache.putEntity(tradingType, bean);
		}
		tradingConfig_Cache.forceReload(false);
		return bean;
	}
}

