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
public class VisibleAttributeUpdater implements IAttributeUpdater<View> {

	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field,Object value) {
		Boolean val = Boolean.class.cast(value);
		if((val != null)&&(attrType == AttributeKeys.visible)){
			Boolean bool = field.getAttribute(AttributeKeys.takeSpaceWhenInvisible);
			boolean takespace = bool != null ? bool.booleanValue() : false;
			if(val.booleanValue()){
				control.setVisibility(View.VISIBLE);
			}else if(takespace){
				control.setVisibility(View.INVISIBLE);
			}else{
				control.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public <T> T updateModel(View control, AttributeKey<T> attrType,
			IUIComponent field) {
		Boolean val = null;
		if(attrType == AttributeKeys.visible){
			val = control.getVisibility() == View.VISIBLE;
			IWritable w = field != null ? field.getAdaptor(IWritable.class) : null;
			if(w != null) {
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
