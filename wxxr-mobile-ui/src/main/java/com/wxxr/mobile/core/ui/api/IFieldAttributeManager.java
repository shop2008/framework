/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IFieldAttributeManager {
	<T> IFieldAttributeManager registerAttribute(String name, Class<T> valueType);
	IFieldAttributeManager unregisterAttribute(String name);
	AttributeKey<?> getAttributeKey(String name);
	<T> IFieldAttributeManager registerAttributeUpdater(String name, IAttributeUpdater<T> updater);
	<T> IFieldAttributeManager unregisterAttributeUpdater(String name, IAttributeUpdater<T> updater);
	IAttributeUpdater<?>[] getAttributeUpdaters(String name);
}
