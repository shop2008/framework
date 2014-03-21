package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.UserCreateTradAccInfoVO;

@NetworkConstraint
public class GetUserCreateTradAccInfoCommand implements ICommand<List<UserCreateTradAccInfoVO>> {
	
    public static final String COMMAND_NAME = "GetUserCreateTradAccInfo";

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Class<List<UserCreateTradAccInfoVO>> getResultType() {
        Class clazz = List.class;
        return clazz;
    }

    @Override
    public void validate() {
    }
    
}