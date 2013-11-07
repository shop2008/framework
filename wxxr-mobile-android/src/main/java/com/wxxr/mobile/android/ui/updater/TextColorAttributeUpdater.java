/**
 * 
 */
package com.wxxr.mobile.android.ui.updater;

import android.view.View;
import android.widget.TextView;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author xijiadeng
 *
 */
public class TextColorAttributeUpdater implements IAttributeUpdater<View> {

	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field, Object value) {
		Integer val = (Integer)value;
		if(!(control instanceof TextView)){
			return;
		}
		TextView tv = (TextView)control;
		if((val != null)&&(attrType == AttributeKeys.textColor)){
			tv.setTextColor(val);
		}
	}

	@Override
	public <T> T updateModel(View control, AttributeKey<T> attrType,
			IUIComponent field) {
		return null;
	}

	@Override
	public boolean acceptable(Object control) {
		return control instanceof View;
	}

}
