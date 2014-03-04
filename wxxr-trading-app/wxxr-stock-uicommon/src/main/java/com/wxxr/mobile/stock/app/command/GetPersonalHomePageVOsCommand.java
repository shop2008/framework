package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;

@NetworkConstraint
@SecurityConstraint(allowRoles={})
public class GetPersonalHomePageVOsCommand implements ICommand<List<PersonalHomePageVO>> {
	
    public static final String COMMAND_NAME = "GetPersonalHomePageCommand";
    

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Class<List<PersonalHomePageVO>> getResultType() {
        Class clazz = List.class;
        return clazz;
    }

    @Override
    public void validate() {
       
    }
    
}