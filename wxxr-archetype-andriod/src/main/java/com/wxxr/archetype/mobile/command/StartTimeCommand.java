/**
 * 
 */
package com.wxxr.archetype.mobile.command;

import com.wxxr.archetype.mobile.service.TimeBean;
import com.wxxr.mobile.core.command.api.ICommand;

/**
 * @author fudapeng
 *
 */
public class StartTimeCommand implements ICommand<TimeBean> {

	public final static String Name="StartTimeCommand";
	private TimeBean bean;
	public StartTimeCommand(TimeBean bean) {
		this.bean = bean;
	}
	
	public TimeBean getBean(){
		return bean;
	}

	@Override
	public String getCommandName() {
		return Name;
	}

	@Override
	public Class<TimeBean> getResultType() {
		return TimeBean.class;
	}

	@Override
	public void validate() {
		
	}

}
