package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class BuyStockHandler extends BasicCommandHandler {
    @Override
    public <T> T execute(ICommand<T> command) throws Exception {
        if (command instanceof BuyStockCommand) {
            BuyStockCommand g = (BuyStockCommand) command;
            StockResultVO vo = getRestService(ITradingProtectedResource.class).buyStock(g.getAcctID(), g.getMarket(), g.getCode(), Long.valueOf(g.getPrice()), Long.valueOf(g.getAmount()));
            return (T) vo;
        }
        return null;
    }

}
