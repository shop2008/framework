/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

import com.wxxr.mobile.core.event.api.IEventObject;

/**
 * @author neillin
 *
 */
public interface DataChangedEvent extends IEventObject {
	IUIComponent getComponent();
	List<AttributeKey<?>> getChangedAttributes();
}
