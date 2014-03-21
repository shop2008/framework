package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.GainPayDetailsVO;

public class GetGainPayDetailCommand implements ICommand<List<GainPayDetailsVO>> {
	
    public static final String COMMAND_NAME = "GetGainPayDetailCommand";
    

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
    public Class<List<GainPayDetailsVO>> getResultType() {
        Class clazz = List.class;
        return clazz;
    }
    @Override
    public void validate() {
      
    }
}