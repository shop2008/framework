/**
 * 
 */
package com.wxxr.mobile.android.ui.updater;

import android.view.View;

import com.wxxr.mobile.android.ui.ImageUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author neillin
 *
 */
public class BackgroupImageURIAttributeUpdater implements IAttributeUpdater<View> {
	private static final Trace log = Trace.register(BackgroupImageURIAttributeUpdater.class);
	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field,Object value) {
		String val = (String)value;
		if((val != null)&&(attrType == AttributeKeys.backgroundImageURI)){
			try {
//				if(RUtils.isResourceIdURI(val)){
//					control.setBackgroundResource(RUtils.getInstance().getResourceIdByURI(val));
//				}else{
//					URL url = new URL(val);
//					control.setBackgroundDrawable(Drawable.createFromStream(url.openStream(), null));
//				}
				ImageUtils.updateViewBackgroupImage(val, control);
			} catch (Exception e) {
				log.error("Failed to set background image for field :"+field.getName(), e);
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
