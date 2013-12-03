package com.wxxr.mobile.stock.trade.command;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class ClearTradingAccountHandler extends BasicCommandHandler {

    @Override
    public <T> T execute(ICommand<T> command) throws Exception {
        if (command instanceof ClearTradingAccountCommand) {
            ClearTradingAccountCommand g = (ClearTradingAccountCommand) command;
            StockResultVO vo = getRestService(ITradingProtectedResource.class).clearTradingAccount(g.getAcctID());
            return (T) vo;
        }
        return null;
    }

}
