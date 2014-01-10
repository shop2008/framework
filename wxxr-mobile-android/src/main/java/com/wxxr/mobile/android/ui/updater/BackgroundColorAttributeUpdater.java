/**
 * 
 */
package com.wxxr.mobile.android.ui.updater;

import android.graphics.Color;
import android.view.View;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author neillin
 *
 */
public class BackgroundColorAttributeUpdater implements IAttributeUpdater<View> {

	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field, Object value) {
		if(value instanceof String){
			String val = (String)value;
			if((val != null)&&(attrType == AttributeKeys.backgroundColor)){
				if(RUtils.isResourceIdURI(val)){
					control.setBackgroundColor(AppUtils.getFramework().getAndroidApplication().getResources().getColor(RUtils.getInstance().getResourceIdByURI(val)));
				}else{
					control.setBackgroundColor(Color.parseColor(val));
				}
			}
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
