/**
 * 
 */
package com.wxxr.mobile.core.ui.impl;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.core.ui.api.AttributeKeys;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.UIError;

/**
 * @author neillin
 *
 */
public class ViewBase extends AbstractUIContainer implements IView {

	public IDataField getDataField(String name) {
		return getChild(name, IDataField.class);
	}

	public List<IDataField> getDataFields() {
		return getChildren(IDataField.class);
	}

	public boolean isActive() {
		Boolean bool = getAttribute(AttributeKeys.visible);
		return bool != null ? bool.booleanValue() : false;
	}

	public void activate() {
		if(!isActive()){
			setAttribute(AttributeKeys.visible, true);
		}
	}

	public void deactivate() {
		if(isActive()){
			setAttribute(AttributeKeys.visible, false);
		}
		
	}

	public List<UIError> getErrors() {
		List<UIError> errors = null;
		for (IDataField fld : getDataFields()) {
			UIError err = fld.getUIError();
			if(err != null){
				if(errors == null){
					errors = new ArrayList<UIError>();
				}
				errors.add(err);
			}
		}
		return errors;
	}}
