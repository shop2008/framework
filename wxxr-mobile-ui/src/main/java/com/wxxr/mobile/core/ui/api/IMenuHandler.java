/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IMenuHandler {
	void showMenu();
	void hideMenu();
	boolean isMenuOnShow();
	void setMenuCallback(IMenuCallback cb);
}
