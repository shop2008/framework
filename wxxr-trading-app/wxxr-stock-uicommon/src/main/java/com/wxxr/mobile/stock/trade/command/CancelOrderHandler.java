package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class CancelOrderHandler  extends BasicCommandHandler {
    @Override
    public <T> T execute(ICommand<T> command) throws Exception {
        if (command instanceof CancelOrderCommand) {
            CancelOrderCommand g = (CancelOrderCommand) command;
            StockResultVO vo = getRestService(ITradingProtectedResource.class).cancelOrder(g.getOrderID());
            return (T) vo;
        }
        return null;
    }

}
