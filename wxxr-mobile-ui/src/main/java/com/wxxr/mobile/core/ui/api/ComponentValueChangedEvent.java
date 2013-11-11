/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface ComponentValueChangedEvent extends ValueChangedEvent {
	List<AttributeKey<?>> getChangedAttributes();
}
