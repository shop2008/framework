/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.Set;

import com.wxxr.mobile.core.microkernel.api.IAdaptable;

/**
 * @author neillin
 *
 */
public interface IUIComponent extends IAdaptable,Cloneable {
	String getName();
	
	<T> T getAttribute(AttributeKey<T> key);
	boolean hasAttribute(AttributeKey<?> key);
	 Set<AttributeKey<?>> getAttributeKeys();
	 
	IUIContainer getParent();
	boolean isSubsidiaryOf(IUIComponent component);
	
	void init(IUIManagementContext ctx);
	void destroy();
	boolean isInitialized();
}
