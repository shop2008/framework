/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import java.util.LinkedList;

import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.InputEvent;

/**
 * @author neillin
 *
 */
public class UICommand extends UIComponent implements IUICommand{
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIComponent#invokeCommand(java.lang.String, com.wxxr.mobile.core.ui.api.InputEvent)
	 */
	@Override
	public void invokeCommand(String cmdName, InputEvent event) {
		if(cmdName == null){
			cmdName = getName();
		}
		super.invokeCommand(cmdName, event);
	}

}
