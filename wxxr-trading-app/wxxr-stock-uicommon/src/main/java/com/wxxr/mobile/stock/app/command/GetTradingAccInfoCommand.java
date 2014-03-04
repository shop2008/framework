package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.TradingAccInfoVO;

@NetworkConstraint(allowConnectionTypes={})
@SecurityConstraint(allowRoles={})
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
