/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IDialog {
	String DIALOG_ATTRIBUTE_TITLE = "title";		// correspondent value should String in format of regular message or resource id url
	String DIALOG_ATTRIBUTE_ICON = "icon";		// correspondent value should String in format of image url or resource id url
	String DIALOG_ATTRIBUTE_MESSAGE = "message";		// correspondent value should String in format of regular message or resource id url
	String DIALOG_ATTRIBUTE_LEFT_BUTTON = "left_button";	// correspondent value should be type of IUICommand
	String DIALOG_ATTRIBUTE_RIGHT_BUTTON = "right_button";	// correspondent value should be type of IUICommand
	String DIALOG_ATTRIBUTE_MID_BUTTON = "mid_button";		// correspondent value should be type of IUICommand
	void dismiss();
	void show();
}
