/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public interface IMenu extends IUICommand{
	String getName();
	String[] getCommandIds();
	IUICommand getCommand(String cmd);
	IMenu addCommand(String cmdId);
	IMenu removeCommand(String cmdId);
	void show();
	void hide();
	boolean isOnShow();
}
