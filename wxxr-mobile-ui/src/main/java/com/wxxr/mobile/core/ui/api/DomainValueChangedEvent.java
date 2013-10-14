/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import com.wxxr.mobile.core.event.api.IEventObject;

/**
 * @author neillin
 *
 */
public interface DomainValueChangedEvent extends IEventObject {
	IDomainValueModel getModel();
}
