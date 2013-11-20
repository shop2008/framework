/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IValidationErrorHandler {
	void handleValidationError(IUIComponent model, Object bindingControl, ValidationError[] errs);
}
