package com.wxxr.mobile.stock.app.service.impl;

import java.util.List;

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
import com.wxxr.mobile.stock.app.bean.MyArticlesBean;
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
import com.wxxr.mobile.stock.app.service.loader.UserCreateTradAccInfoLoader;
import com.wxxr.mobile.stock.trade.command.CreateTradingAccountCommand;
import com.wxxr.mobile.stock.trade.command.CreateTradingAccountHandler;
import com.wxxr.mobile.stock.trade.entityloader.TradingAccInfoLoader;
import com.wxxr.stock.trading.ejb.api.StockResultVO;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;

public class NewTradingManagementServiceImpl extends AbstractModule<IStockAppContext> implements ITradingManagementService {
    private static final Trace log = Trace.register(NewTradingManagementServiceImpl.class);

    private GenericReloadableEntityCache<String,TradingAccInfoBean,List> tradingAccInfo_cache;

    //
    protected UserCreateTradAccInfoBean userCreateTradAccInfo;
    private IReloadableEntityCache<String, UserCreateTradAccInfoBean> userCreateTradAccInfo_Cache;

    
    public BindableListWrapper<TradingAccInfoBean> getT0TradingAccountList(){
        BindableListWrapper<TradingAccInfoBean> t0s = tradingAccInfo_cache.getEntities(new IEntityFilter<TradingAccInfoBean>(){
            @Override
            public boolean doFilter(TradingAccInfoBean entity) {
                if (entity.getStatus()==1){
                    return true;
                }
                return false;
            }
            
        }, null);
        return t0s;
    }
    public BindableListWrapper<TradingAccInfoBean> getT1TradingAccountList(){
        BindableListWrapper<TradingAccInfoBean> t1s = tradingAccInfo_cache.getEntities(new IEntityFilter<TradingAccInfoBean>(){
            @Override
            public boolean doFilter(TradingAccInfoBean entity) {
                if (entity.getStatus()!=1){
                    return true;
                }
                return false;
            }
            
        }, null);
        return t1s;
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
        
    }

    @Override
    public void sellStock(String acctID, String market, String code, String price, String amount) throws StockAppBizException {
        
    }
    
    
    @Override
    public void cancelOrder(String orderID) {
        
    }

    @Override
    public void clearTradingAccount(String acctID) {
        
    }
    @Override
    public void quickBuy(Long captitalAmount, String capitalRate, boolean virtual, String stockMarket, String stockCode, String stockBuyAmount, String depositRate) throws StockAppBizException {
        
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
    
    //未结算
    @Override
    public TradingAccountBean getTradingAccountInfo(String acctID) throws StockAppBizException {
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
    protected void startService() {
        context.registerService(ITradingManagementService.class, this);

        IEntityLoaderRegistry registry = getService(IEntityLoaderRegistry.class);
        tradingAccInfo_cache=new GenericReloadableEntityCache<String,TradingAccInfoBean,List>("tradingAccInfo",30);
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("tradingAccInfo", new TradingAccInfoLoader());
        registry.registerEntityLoader("UserCreateTradAccInfo", new UserCreateTradAccInfoLoader());
        context.getService(ICommandExecutor.class).registerCommandHandler(CreateTradingAccountCommand.Name, new CreateTradingAccountHandler());

    }

    @Override
    protected void stopService() {
        context.unregisterService(ITradingManagementService.class, this);
    }
    @Override
    public BindableListWrapper<EarnRankItemBean> getEarnRank(int start, int limit) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void reloadEarnRank(int start, int limit, boolean wait4Finish) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public BindableListWrapper<MegagameRankBean> getTMegagameRank() throws StockAppBizException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void reloadTMegagameRank(boolean wait4Finish) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public BindableListWrapper<MegagameRankBean> getT1MegagameRank() throws StockAppBizException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void reloadT1MegagameRank(boolean wait4Finish) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public BindableListWrapper<RegularTicketBean> getRegularTicketRank() throws StockAppBizException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void reloadRegularTicketRank(boolean wait4Finish) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public BindableListWrapper<WeekRankBean> getWeekRank() throws StockAppBizException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void reloadWeekRank(boolean wait4Finish) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public BindableListWrapper<GainBean> getTotalGain(int start, int limit) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public BindableListWrapper<GainBean> getGain(int start, int limit) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
