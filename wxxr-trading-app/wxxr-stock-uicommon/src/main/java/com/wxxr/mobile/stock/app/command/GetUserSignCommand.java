package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.UserSignVO;

@NetworkConstraint
@SecurityConstraint(allowRoles={})
public class GetUserSignCommand implements ICommand<List<UserSignVO>>{
	
	public static final String COMMAND_NAME = "GetUserSignCommand";


	public String getCommandName() {
		return COMMAND_NAME;
	}
	public void validate() {
		
		
	}
	public Class<List<UserSignVO>> getResultType() {
		Class clazz=List.class;
		return clazz;
	}
	
}
