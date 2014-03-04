package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.PersonalHomePageVO;

@NetworkConstraint
class GetOtherPersonalHomePageLoader implements ICommand<List<PersonalHomePageVO>> {
    private String userId;
    @Override
    public String getCommandName() {
        return OtherPersonalHomePageLoader.COMMAND_NAME;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Class<List<PersonalHomePageVO>> getResultType() {
        Class clazz = List.class;
        return clazz;
    }

    @Override
    public void validate() {
        if (this.userId==null){
            throw new IllegalArgumentException();
        }
       
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }   
}