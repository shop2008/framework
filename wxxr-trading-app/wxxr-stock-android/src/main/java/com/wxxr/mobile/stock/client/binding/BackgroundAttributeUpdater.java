/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import android.graphics.Color;
import android.view.View;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;

/**
 * @author xijiadeng
 *
 */
public class BackgroundAttributeUpdater implements IAttributeUpdater<View> {

	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field, Object value) {
		if(!(control instanceof View)){
			return;
		}
		View tv = (View)control;
		if(value instanceof String){
			String val = (String)value;
			if(val != null){
				if(RUtils.isResourceIdURI(val)){
					tv.setBackgroundDrawable(AppUtils.getFramework().getAndroidApplication().getResources().getDrawable(RUtils.getInstance().getResourceIdByURI(val)));
				}else{
					tv.setBackgroundColor(Color.parseColor(val));
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
