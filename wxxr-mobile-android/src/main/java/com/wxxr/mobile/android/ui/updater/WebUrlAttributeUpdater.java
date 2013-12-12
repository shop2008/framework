/**
 * 
 */
package com.wxxr.mobile.android.ui.updater;

import android.view.View;
import android.webkit.WebView;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author xijiadeng
 *
 */
public class WebUrlAttributeUpdater implements IAttributeUpdater<View> {

	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field, Object value) {
		String val = (String)value;
		if(!(control instanceof WebView)){
			return;
		}
		WebView wv = (WebView)control;
		if((val != null)&&(attrType == AttributeKeys.webUrl)){
			wv.getSettings().setJavaScriptEnabled(true);
			wv.loadUrl(val);
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
