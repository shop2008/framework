package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class QuickBuyStockHandler extends BasicCommandHandler{

    @Override
    public <T> T execute(ICommand<T> command) throws Exception {
        if (command instanceof QuickBuyStockCommand) {
            QuickBuyStockCommand g = (QuickBuyStockCommand) command;
            final float _capitalRate = Float.valueOf(g.getCapitalRate());
            final long _stockBuyAmount = Long.valueOf(g.getStockBuyAmount());
            final float _depositRate = Float.valueOf(g.getDepositRate());
            
            StockResultVO vo =  getRestService(ITradingProtectedResource.class).mulQuickBuy(
                    g.getCaptitalAmount(), _capitalRate, g.isVirtual(), g.getStockMarket(),
                    g.getStockCode(), _stockBuyAmount, _depositRate, g.getAssetType());
            
            return (T) vo;
        }
        return null;    
        }

}
