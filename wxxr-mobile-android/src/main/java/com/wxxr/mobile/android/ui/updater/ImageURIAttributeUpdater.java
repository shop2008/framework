/**
 * 
 */
package com.wxxr.mobile.android.ui.updater;

import java.net.URL;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author neillin
 *
 */
public class ImageURIAttributeUpdater implements IAttributeUpdater<View> {
	private static final Trace log = Trace.register(ImageURIAttributeUpdater.class);
	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field,Object value) {
		if(!(control instanceof ImageView)){
			return;
		}
		ImageView imgV = (ImageView)control;
		String val = (String)value;
		if((val != null)&&(attrType == AttributeKeys.imageURI)){
			try {
				if(RUtils.isResourceIdURI(val)){
					imgV.setImageResource(RUtils.getInstance().getResourceIdByURI(val));
				}else{
					URL url = new URL(val);
					imgV.setImageDrawable(Drawable.createFromStream(url.openStream(), null));
				}
			} catch (Exception e) {
				log.error("Failed to set image for field :"+field.getName(), e);
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
		return control instanceof ImageView;
	}

}
