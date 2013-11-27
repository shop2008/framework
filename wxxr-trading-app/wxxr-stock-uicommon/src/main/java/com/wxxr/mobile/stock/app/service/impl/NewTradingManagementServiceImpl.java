package com.wxxr.mobile.stock.app.service.impl;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommandExecutor;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.mobile.stock.app.bean.AuditDetailBean;
import com.wxxr.mobile.stock.app.bean.DealDetailBean;
import com.wxxr.mobile.stock.app.bean.EarnRankItemBean;
import com.wxxr.mobile.stock.app.bean.MegagameRankBean;
import com.wxxr.mobile.stock.app.bean.RankListBean;
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
import com.wxxr.mobile.stock.app.service.ITradingManagementService;
import com.wxxr.mobile.stock.trade.entityloader.TradingAccInfoLoader;

public class NewTradingManagementServiceImpl extends AbstractModule<IStockAppContext> implements ITradingManagementService {
    //交易账户缓存
    GenericReloadableEntityCache<String,TradingAccInfoBean,List> tradingAccInfo_cache;
    
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
    @Override
    public TradingAccountListBean getHomePageTradingAccountList() throws StockAppBizException {
        TradingAccountListBean b=new TradingAccountListBean();
//        b.setT0TradingAccounts(getT0TradingAccountList());
        
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
    public TradingAccountBean getTradingAccountInfo(String acctID) throws StockAppBizException {
        return null;
    }

    @Override
    public void createTradingAccount(Long captitalAmount, float capitalRate, boolean virtual, float depositRate) throws StockAppBizException {
        
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

    @Override
    public DealDetailBean getDealDetail(String accId) {
        return null;
    }

    @Override
    public AuditDetailBean getAuditDetail(String accId) {
        return null;
    }

  

   

    @Override
    public UserCreateTradAccInfoBean getUserCreateTradAccInfo() {
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
        tradingAccInfo_cache=new GenericReloadableEntityCache<String,TradingAccInfoBean,List>("tradingAccInfo",30);
        context.getService(IEntityLoaderRegistry.class).registerEntityLoader("tradingAccInfo", new TradingAccInfoLoader());
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
    
    
}
