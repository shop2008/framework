/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.UIConstants;

/**
 * @author neillin
 *
 */
public class UICommand extends UIComponent implements IUICommand{
	private String cmdName;
	private IUICommandHandler handler;
	private Object handback;
	
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
	public void handleInputEvent(InputEvent event) {
		String command = event.getTargetCommand();
		if((this.handback != null)&&(event instanceof SimpleInputEvent)){
			((SimpleInputEvent)event).addProperty(UIConstants.MESSAGEBOX_ATTRIBUTE_HANDBACK, this.handback);
		}
		if(command == null){
			if(this.handler != null){
				getUIContext().getWorkbenchManager().getCommandExecutor().executeCommand(this.cmdName, null, this.handler, event);
				return;
			}
			command = this.cmdName == null ? getName() : this.cmdName;
			event.setTargetCommand(command);
		}
		super.handleInputEvent(event);
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

	/**
	 * @return the handback
	 */
	public Object getHandback() {
		return handback;
	}

	/**
	 * @param handback the handback to set
	 */
	public void setHandback(Object handback) {
		this.handback = handback;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UICommand:" + getName()+"/"+cmdName + "]";
	}

}
