package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.crm.customizing.ejb.api.UserAttributeVO;

public class GetUserAttributesCommand implements ICommand<List<UserAttributeVO>>{

	public final static String COMMAND_NAME="GetUserAttributes";
	

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
	 */
	@Override
	public Class<List<UserAttributeVO>> getResultType() {
		Class clazz=List.class;
		return clazz;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
	 */
	@Override
	public void validate() {
		
	}
	
}