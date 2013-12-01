package com.wxxr.mobile.stock.app.service.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wxxr.mobile.stock.app.bean.TradingAccInfoBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountBean;
import com.wxxr.mobile.stock.app.bean.TradingAccountListBean;
import com.wxxr.mobile.stock.app.bean.TradingRecordListBean;
import com.wxxr.mobile.stock.app.bean.UserCreateTradAccInfoBean;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.common.BindableListWrapper;
import com.wxxr.mobile.stock.app.common.GenericReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.IEntityFilter;
import com.wxxr.mobile.stock.app.common.IEntityLoaderRegistry;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.app.service.loader.TradingAccountInfoLoader;
import com.wxxr.mobile.stock.app.service.loader.UserCreateTradAccInfoLoader;
import com.wxxr.mobile.stock.trade.command.BuyStockCommand;
import com.wxxr.mobile.stock.trade.command.BuyStockHandler;
import com.wxxr.mobile.stock.trade.command.CreateTradingAccountCommand;
import com.wxxr.mobile.stock.trade.command.CreateTradingAccountHandler;
import com.wxxr.mobile.stock.trade.command.QuickBuyStockCommand;
import com.wxxr.mobile.stock.trade.command.QuickBuyStockHandler;
import com.wxxr.mobile.stock.trade.command.SellStockCommand;
import com.wxxr.mobile.stock.trade.command.SellStockHandler;
import com.wxxr.mobile.stock.trade.entityloader.TradingAccInfoLoader;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;

public class NewTradingManagementServiceImpl extends AbstractModule<IStockAppContext> implements ITradingManagementService {
    private static final Trace log = Trace.register(NewTradingManagementServiceImpl.class);
    //交易盘
    private GenericReloadableEntityCache<String,TradingAccInfoBean,List> tradingAccInfo_cache;
    //交易盘详细信息
    private GenericReloadableEntityCache<Long,TradingAccountBean,List> tradingAccountBean_cache;
    protected UserCreateTradAccInfoBean userCreateTradAccInfo;
    private IReloadableEntityCache<String, UserCreateTradAccInfoBean> userCreateTradAccInfo_Cache;

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
    protected void startService() {
        context.registerService(ITradingManagementService.class, this);

        IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
        tradingAccInfo_cache=new GenericReloadableEntityCache<String,TradingAccInfoBean,List>("tradingAccInfo",30);
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("tradingAccInfo", new TradingAccInfoLoader());
        registry.registerEntityLoader("UserCreateTradAccInfo", new UserCreateTradAccInfoLoader());
        tradingAccountBean_cache=new GenericReloadableEntityCache<Long, TradingAccountBean, List>("TradingAccountInfo");
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("TradingAccountInfo", new TradingAccountInfoLoader());
        context.getService(ICommandExecutor.class).registerCommandHandler(CreateTradingAccountCommand.Name, new CreateTradingAccountHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(BuyStockCommand.Name, new BuyStockHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(SellStockCommand.Name, new SellStockHandler());
        context.getService(ICommandExecutor.class).registerCommandHandler(QuickBuyStockCommand.Name, new QuickBuyStockHandler());

    }
    @Override
    public void cancelOrder(String orderID) {
        
    }

    @Override
    public void clearTradingAccount(String acctID) {
        
    }
   
    

    //已结算 
    @Override
    public DealDetailBean getDealDetail(String accId) {
        return null;
    }

    @Override
    public AuditDetailBean getAuditDetail(String accId) {
        return null;
    }
    
  
    
    @Override
    public TradingAccountListBean getMyAllTradingAccountList(int strat, int limit) {
        return null;
    }

    @Override
    public TradingAccountListBean getMySuccessTradingAccountList(int strat, int limit) {
        return null;
    }



    
    

    

    

   


  

   

    

    @Override
    public TradingRecordListBean getTradingAccountRecord(String acctID, int start, int limit) {
        return null;
    }

    @Override
    protected void initServiceDependency() {
        addRequiredService(IRestProxyService.class);
        addRequiredService(ICommandExecutor.class);
        addRequiredService(IEntityLoaderRegistry.class);

        
    }

    

    @Override
    protected void stopService() {
        context.unregisterService(ITradingManagementService.class, this);
    }
    @Override
    public BindableListWrapper<EarnRankItemBean> getEarnRank(int start, int limit) {
        return null;
    }
    @Override
    public void reloadEarnRank(int start, int limit, boolean wait4Finish) {
        
    }
    @Override
    public BindableListWrapper<MegagameRankBean> getTMegagameRank() throws StockAppBizException {
        return null;
    }
    @Override
    public void reloadTMegagameRank(boolean wait4Finish) {
        
    }
    @Override
    public BindableListWrapper<MegagameRankBean> getT1MegagameRank() throws StockAppBizException {
        return null;
    }
    @Override
    public void reloadT1MegagameRank(boolean wait4Finish) {
        
    }
    @Override
    public BindableListWrapper<RegularTicketBean> getRegularTicketRank() throws StockAppBizException {
        return null;
    }
    @Override
    public void reloadRegularTicketRank(boolean wait4Finish) {
        
    }
    @Override
    public BindableListWrapper<WeekRankBean> getWeekRank() throws StockAppBizException {
        return null;
    }
    @Override
    public void reloadWeekRank(boolean wait4Finish) {
        
    }
    @Override
    public BindableListWrapper<GainBean> getTotalGain(int start, int limit) {
        return null;
    }
    @Override
    public BindableListWrapper<GainBean> getGain(int start, int limit) {
        return null;
    }
    
    
}
