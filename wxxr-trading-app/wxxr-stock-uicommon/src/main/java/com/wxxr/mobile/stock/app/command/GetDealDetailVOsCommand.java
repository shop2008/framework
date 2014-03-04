package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.DealDetailInfoVO;

@NetworkConstraint
public class GetDealDetailVOsCommand implements ICommand<List<DealDetailInfoVO>> {
	
    public static final String COMMAND_NAME = "GetDealDetailCommand";

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
    public Class<List<DealDetailInfoVO>> getResultType() {
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