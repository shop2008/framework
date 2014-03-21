package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.TradingAccountVO;

@NetworkConstraint
public class GetTradingAccountInfoCommand implements ICommand<List<TradingAccountVO>> {
	
   public final static String COMMAND_NAME = "GetTradingAccountInfo";

   private String acctID;
    public String getAcctID() {
        return acctID;
    }

    public void setAcctID(String acctID) {
        this.acctID = acctID;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Class<List<TradingAccountVO>> getResultType() {
        Class clazz = List.class;
        return clazz;
    }

    @Override
    public void validate() {
    }
    
}