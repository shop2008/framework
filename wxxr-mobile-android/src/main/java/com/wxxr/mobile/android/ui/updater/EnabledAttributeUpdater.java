/**
 * 
 */
package com.wxxr.mobile.android.ui.updater;

import android.view.View;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IWritable;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author neillin
 *
 */
public class EnabledAttributeUpdater implements IAttributeUpdater<View> {

	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field,Object value) {
		Boolean val = Boolean.class.cast(value);
		if((val != null)&&(attrType == AttributeKeys.enabled)){
			if(val.booleanValue()){
				control.setEnabled(true);
			}else{
				control.setEnabled(false);
			}
		}
	}

	@Override
	public <T> T updateModel(View control, AttributeKey<T> attrType,
			IUIComponent field) {
		Boolean val = null;
		if(attrType == AttributeKeys.enabled){
			val = control.isEnabled();
			IWritable w = field != null ? field.getAdaptor(IWritable.class) : null;
			if(w != null){
				w.setValue(val);
			}
		}
		return val != null ? attrType.getValueType().cast(val) : null;
	}

	@Override
	public boolean acceptable(Object control) {
		return control instanceof View;
	}
}
