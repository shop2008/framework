/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface InputEvent {
	String EVENT_TYPE_CLICK = "Click";
	String EVENT_TYPE_LONGCLICK = "LongClick";
	String EVENT_TYPE_SELECTION_CHANGED = "SelectionChanged";
	String EVENT_TYPE_TEXT_CHANGED = "TextChanged";
	String EVENT_TYPE_SWIPE_LEFT = "SwipeLeft";
	String EVENT_TYPE_SWIPE_RIGHT = "SwipeRight";
	String EVENT_TYPE_SWIPE_UP = "SwipeUp";
	String EVENT_TYPE_SWIPE_DOWN = "SwipeDown";
	
	String getEventType();
	IUIComponent getEventSource();
	String[] getPropertyKeys();
	Object getProperty(String key);
	boolean hasProperty(String key);
}
