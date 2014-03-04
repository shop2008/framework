package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.DrawMoneyRecordVo;

public class GetDrawMoneyRecordCommand implements ICommand<List<DrawMoneyRecordVo>> {
	
    public static final String COMMAND_NAME = "GetDrawMoneyRecordCommand";

    private int start, limit;
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
    public Class<List<DrawMoneyRecordVo>> getResultType() {
        Class clazz = List.class;
        return clazz;
    }
    @Override
    public void validate() {
      
    }
}
