package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class SellStockHandler extends BasicCommandHandler {
    @Override
    public <T> T execute(ICommand<T> command) throws Exception {
        if (command instanceof SellStockCommand) {
            SellStockCommand g = (SellStockCommand) command;
            StockResultVO vo = getRestService(ITradingProtectedResource.class).sellStock(g.getAcctID(), g.getMarket(), g.getCode(), Long.valueOf(g.getPrice()), Long.valueOf(g.getAmount()));
            return (T) vo;
        }
        return null;
    }
}
