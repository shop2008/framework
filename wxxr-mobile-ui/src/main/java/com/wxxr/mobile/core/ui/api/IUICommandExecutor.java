/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IUICommandExecutor {
	/**
	 * 
	 * @param cmdName : name correspondent to passed-in command handler
	 * @param view : presentation model user interacting with
	 * @param command : command handler
	 * @param args : arguments
	 */
	void executeCommand(String cmdName,IView view,ICommandHandler command, InputEvent event);
}
