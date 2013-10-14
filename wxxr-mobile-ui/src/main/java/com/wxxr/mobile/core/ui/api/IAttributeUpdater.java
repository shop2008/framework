/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * 
 * @author neillin
 *
 */
public interface IAttributeUpdater<W> {
	/**
	 * update attribute value from presentation model to UI control
	 * @param control
	 * @param attrType
	 * @param field
	 * @return the value was updated
	 */
	<T> void updateControl(W control, AttributeKey<T> attrType, IUIComponent field, Object value);
	
	/**
	 * update attribute value from control to presentation model, if passed-in field is null,
	 * updating will not happen, only the value will be retrieved from control and returned
	 * @param control
	 * @param attrType
	 * @param field : presentation model, could be null
	 * @return the value was updated
	 */
	<T> T updateModel(W control,AttributeKey<T> attrType, IUIComponent field);
	
	boolean acceptable(Object control);
}
