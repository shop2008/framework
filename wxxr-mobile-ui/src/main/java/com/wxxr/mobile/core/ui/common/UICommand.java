/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.InputEvent;

/**
 * @author neillin
 *
 */
public class UICommand extends UIComponent implements IUICommand{
	private String cmdName;
	private IUICommandHandler handler;
	
	public UICommand() {
		super();
	}

	public UICommand(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.ui.common.UIComponent#invokeCommand(java.lang.String, com.wxxr.mobile.core.ui.api.InputEvent)
	 */
	@Override
	public void invokeCommand(String command, InputEvent event) {
		if(command == null){
			if(this.handler != null){
				getUIContext().getWorkbenchManager().getCommandExecutor().executeCommand(this.cmdName, null, this.handler, event);
				return;
			}
			command = this.cmdName == null ? getName() : this.cmdName;
		}
		super.invokeCommand(command, event);
	}
	
	/**
	 * @return the cmdName
	 */
	public String getCommandName() {
		return cmdName;
	}
	/**
	 * @param cmdName the cmdName to set
	 */
	public void setCommandName(String cmdName) {
		this.cmdName = cmdName;
	}

	/**
	 * @return the handler
	 */
	public IUICommandHandler getHandler() {
		return handler;
	}

	/**
	 * @param handler the handler to set
	 */
	public void setHandler(IUICommandHandler handler) {
		this.handler = handler;
	}

}
