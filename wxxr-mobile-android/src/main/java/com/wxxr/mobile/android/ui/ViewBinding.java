/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.view.View;

import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IUIComponent;

/**
 * @author neillin
 *
 */
public class ViewBinding extends UIContainerBinding {

	public ViewBinding(View view, IUIComponent field) {
		super(view, field);
	}


	@Override
	protected String getUIName(IUIComponent field) {
		if(field instanceof IDataField){
			return "fld_"+field.getName();
		}
		if(field instanceof IUICommand){
			return "cmd_"+field.getName();
		}
		throw new IllegalArgumentException("UNKnown field :"+field);
	}


	@Override
	protected String getChildBindingFactoryName(IUIComponent view, View ui) {
		return "field_binding";
	}

}
