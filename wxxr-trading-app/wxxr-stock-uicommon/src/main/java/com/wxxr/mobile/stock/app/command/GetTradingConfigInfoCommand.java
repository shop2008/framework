package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.TradingConfigVO;

@NetworkConstraint
public class GetTradingConfigInfoCommand implements ICommand<List<TradingConfigVO>> {

    public static final String COMMAND_NAME = "GetTradingConfigInfoCommand";

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Class<List<TradingConfigVO>> getResultType() {
        Class clazz = List.class;
        return clazz;
    }

    @Override
    public void validate() {
    }
    
}
