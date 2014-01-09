/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IDialog {
	String DIALOG_ATTRIBUTE_TITLE = UIConstants.MESSAGEBOX_ATTRIBUTE_TITLE;
	String DIALOG_ATTRIBUTE_ICON = UIConstants.MESSAGEBOX_ATTRIBUTE_ICON;
	String DIALOG_ATTRIBUTE_MESSAGE = UIConstants.MESSAGEBOX_ATTRIBUTE_MESSAGE;
	String DIALOG_ATTRIBUTE_LEFT_BUTTON = UIConstants.MESSAGEBOX_ATTRIBUTE_LEFT_BUTTON;
	String DIALOG_ATTRIBUTE_RIGHT_BUTTON = UIConstants.MESSAGEBOX_ATTRIBUTE_RIGHT_BUTTON;
	String DIALOG_ATTRIBUTE_MID_BUTTON = UIConstants.MESSAGEBOX_ATTRIBUTE_MID_BUTTON;
	void dismiss();
	void show();
	boolean isOnShow();
	void setCancelable(boolean cancelable);
}
