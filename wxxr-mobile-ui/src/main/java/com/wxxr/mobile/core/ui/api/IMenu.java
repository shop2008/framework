/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public interface IMenu extends IUIComponent{
	String getName();
	String[] getCommandIds();
	IUICommand getCommand(String cmd);
	IMenu addCommand(String cmdId);
	IMenu removeCommand(String cmdId);
}
