package com.wxxr.mobile.stock.app.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.restful.json.SimpleVO;

@NetworkConstraint
public class GetGuideGainRuleCommand implements ICommand<SimpleVO>{
	
	public static final String COMMAND_NAME="GetGuideGainRuleCommand";
	

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public Class<SimpleVO> getResultType() {
		return SimpleVO.class;
	}

	@Override
	public void validate() {
	}
}
