package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.GainVO;

public class GetGainBeanCommand implements ICommand<List<GainVO>> {
	
    public static final String COMMAND_NAME = "GetGainBeanCommand";
    

    int start;
    int limit;
    boolean virtual;
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Class<List<GainVO>> getResultType() {
        Class clazz = List.class;
        return clazz;
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

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }
    
    @Override
    public void validate() {
    }
    
}