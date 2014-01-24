/**
 * 
 */
package com.wxxr.mobile.bind;

import android.graphics.Color;
import android.graphics.Typeface;
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
 * @author fudapeng
 *
 */
public class TypeFaceAttributeUpdater implements IAttributeUpdater<TextView> {

	@Override
	public boolean acceptable(Object arg0) {
		return arg0 instanceof TextView;
	}

	@Override
	public <T> void updateControl(TextView control, AttributeKey<T> attrType,
			IUIComponent field, Object value) {
		TextView tv = (TextView)control;
		if(value instanceof String){
			 String val = (String)value;
			 if(StringUtils.isBlank(val)){
				 return;
			 }
			if((val != null)&&(attrType.getName().equals("typeface"))){
				if("NORMAL".equals(value)){
					tv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
				}else if("ITALIC".equals(value)){
					tv.setTypeface(Typeface.DEFAULT, Typeface.ITALIC);
				}
			}
		}
	}

	@Override
	public <T> T updateModel(TextView arg0, AttributeKey<T> arg1,
			IUIComponent arg2) {
		return null;
	}

}
