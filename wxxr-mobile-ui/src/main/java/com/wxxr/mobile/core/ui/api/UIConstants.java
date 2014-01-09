/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface UIConstants {
	String MESSAGEBOX_ATTRIBUTE_TITLE = "title";		// correspondent value should String in format of regular message or resource id url
	String MESSAGEBOX_ATTRIBUTE_ICON = "icon";		// correspondent value should String in format of image url or resource id url
	String MESSAGEBOX_ATTRIBUTE_MESSAGE = "message";		// correspondent value should String in format of regular message or resource id url
	String MESSAGEBOX_ATTRIBUTE_LEFT_BUTTON = "left_button";	// correspondent value should be type of IUICommand
	String MESSAGEBOX_ATTRIBUTE_RIGHT_BUTTON = "right_button";	// correspondent value should be type of IUICommand
	String MESSAGEBOX_ATTRIBUTE_MID_BUTTON = "mid_button";		// correspondent value should be type of IUICommand
	String MESSAGEBOX_ATTRIBUTE_ON_CANCEL = "onCanceled";		// correspondent value should be type of String, which should be the name of a command in view/page model
	String MESSAGEBOX_ATTRIBUTE_ON_OK = "onOK";					// correspondent value should be type of String, which should be the name of a command in view/page model
	String MESSAGEBOX_ATTRIBUTE_AUTO_CLOSED = "autoClosed";		// correspondent value should be type of int, which indicates the message box should be closed after the specific seconds elapsed
	String MESSAGEBOX_ATTRIBUTE_HANDBACK = "handback";
	String MESSAGEBOX_ATTRIBUTE_CANCELABLE = "cancelable";
	
	String HOME_PAGE_ID = "home";
	String MESSAGE_BOX_ID = "messageBox";
	String TOOL_BAR_VIEW_ID = "toolbarView";
	String PROGRESSMONITOR_DIALOG_ID = "progressMonitor";

}
