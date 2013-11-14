package com.wxxr.mobile.android.ui.updater;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

public class CheckBoxAttributeUpdater implements IAttributeUpdater<View> {
	private static final Trace log = Trace.register(TextAttributeUpdater.class);
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field, Object value) {
		if(!(control instanceof CheckBox || control instanceof RadioButton)){
			return;
		}
		CompoundButton cb = null;
		if(control instanceof CheckBox) {
			cb = (CheckBox)control;
		} else if(control instanceof RadioButton) {
			cb = (RadioButton)control;
		}
		
		Boolean val = (Boolean)value;
		
		
		if((val != null)&&(attrType == AttributeKeys.checked)){
			try {
				if(cb!=null)
					cb.setChecked(val);
			} catch (Exception e) {
				log.error("Failed to set CheckBox/RadioButton for field :"+field.getName(), e);
			}
		}
	}

	public <T> T updateModel(View control, AttributeKey<T> attrType,
			IUIComponent field) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean acceptable(Object control) {
		return control instanceof View;
	}

}
