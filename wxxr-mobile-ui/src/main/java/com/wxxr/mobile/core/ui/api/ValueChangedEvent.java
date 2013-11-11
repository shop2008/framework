/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import com.wxxr.mobile.core.event.api.IEventObject;

/**
 * @author neillin
 *
 */
public interface ValueChangedEvent extends IEventObject {
	String getSourceName();
}
