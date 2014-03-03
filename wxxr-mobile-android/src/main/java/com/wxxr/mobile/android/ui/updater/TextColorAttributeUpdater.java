/**
 * 
 */
package com.wxxr.mobile.android.ui.updater;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author xijiadeng
 *
 */
public class TextColorAttributeUpdater implements IAttributeUpdater<View> {

	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field, Object value) {
		if(!(control instanceof TextView)){
			return;
		}
		TextView tv = (TextView)control;
		if(value instanceof String){
			 String val = (String)value;
			 if(StringUtils.isBlank(val)){
				 return;
			 }
			if((val != null)&&(attrType == AttributeKeys.textColor)){
				if(RUtils.isResourceIdURI(val)){
					tv.setTextColor(AppUtils.getFramework().getAndroidApplication().getResources().getColor(RUtils.getInstance().getResourceIdByURI(val)));
				}else{
					tv.setTextColor(Color.parseColor(val));
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
