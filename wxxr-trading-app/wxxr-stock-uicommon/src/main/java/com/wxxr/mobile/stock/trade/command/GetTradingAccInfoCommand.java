package com.wxxr.mobile.stock.trade.command;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;

public class GetTradingAccInfoCommand implements ICommand<List<TradingAccInfoVO>> {
    public final static String Name="GetTradingAccInfoCommand";
    @Override
    public String getCommandName() {
        return Name;
    }

    @Override
    public Class getResultType() {
        return List.class;
    }

    @Override
    public void validate() {
        
    }

}
