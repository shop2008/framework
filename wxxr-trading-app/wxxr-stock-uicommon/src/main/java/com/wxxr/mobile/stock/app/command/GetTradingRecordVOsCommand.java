package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.TradingRecordVO;

public class GetTradingRecordVOsCommand implements ICommand<List<TradingRecordVO>> {
    public static final String COMMAND_NAME = "GetTradingRecordCommand";
    

    private String acctID;
    private int start;
    private int limit;

    public String getAcctID() {
        return acctID;
    }

    public void setAcctID(String acctID) {
        this.acctID = acctID;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Class<List<TradingRecordVO>> getResultType() {
        Class clazz = List.class;
        return clazz;
    }

    @Override
    public void validate() {
        if(this.acctID == null){
            throw new IllegalArgumentException();
        }
    }
    
}