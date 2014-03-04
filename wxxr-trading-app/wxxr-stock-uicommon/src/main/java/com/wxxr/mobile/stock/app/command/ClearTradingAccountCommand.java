package com.wxxr.mobile.stock.app.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

@NetworkConstraint
public class ClearTradingAccountCommand implements ICommand<StockResultVO>{
    public final static String Name="ClearTradingAccountCommand";
    private String acctID;

    public String getAcctID() {
        return acctID;
    }
    public void setAcctID(String acctID) {
        this.acctID = acctID;
    }
    public ClearTradingAccountCommand(String acctID) {
        super();
        this.acctID = acctID;
    }
    @Override
    public String getCommandName() {
        return Name;
    }
    @Override
    public Class<StockResultVO> getResultType() {
        return StockResultVO.class;
    }
    @Override
    public void validate() {
        if(this.acctID == null){
            throw new IllegalArgumentException();
        }
    }

}
