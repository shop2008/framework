/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;


/**
 * @author neillin
 *
 */
public interface DomainValueChangedEvent extends ValueChangedEvent {
	List<String> getChangedProperties();
}
