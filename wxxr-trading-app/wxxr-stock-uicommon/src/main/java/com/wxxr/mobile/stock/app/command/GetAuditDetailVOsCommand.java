package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.AuditInfoVO;

@NetworkConstraint
public class GetAuditDetailVOsCommand implements ICommand<List<AuditInfoVO>> {
	
    public static final String COMMAND_NAME = "GetAuditDetailVOsCommand";

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
    public Class<List<AuditInfoVO>> getResultType() {
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