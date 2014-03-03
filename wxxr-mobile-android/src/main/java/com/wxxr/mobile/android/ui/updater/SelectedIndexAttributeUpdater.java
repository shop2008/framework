/**
 * 
 */
package com.wxxr.mobile.android.ui.updater;

import android.view.View;
import android.widget.RadioGroup;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;

/**
 * @author xijiadeng
 *
 */
public class SelectedIndexAttributeUpdater implements IAttributeUpdater<View> {

	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field, Object value) {
		if(!(control instanceof RadioGroup)){
			return;
		}
		RadioGroup rg = (RadioGroup)control;
		if(value instanceof Integer){
			 int val = (Integer)value;
			 rg.check(val);
		}
		
	}

	@Override
	public <T> T updateModel(View control, AttributeKey<T> attrType,
			IUIComponent field) {
		return null;
	}

	@Override
	public boolean acceptable(Object control) {
		return control instanceof RadioGroup;
	}

}
