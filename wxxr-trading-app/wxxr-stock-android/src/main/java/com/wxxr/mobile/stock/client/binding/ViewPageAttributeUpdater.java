/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IWritable;
import com.wxxr.mobile.stock.client.widget.MinuteLineViewKeys;

/**
 *
 */
public class ViewPageAttributeUpdater implements IAttributeUpdater<View> {
	private static final Trace log = Trace.register(ViewPageAttributeUpdater.class);
	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field,Object value) {
		if(!(control instanceof ViewPager)){
			return;
		}
		ViewPager tv = (ViewPager)control;
		if(attrType == MinuteLineViewKeys.viewPosition){
			int flag = (Integer) field.getAttribute(attrType);
			if(flag>=0){
				tv.setCurrentItem(flag);
			}
		}
	}

	@Override
	public <T> T updateModel(View control, AttributeKey<T> attrType,
			IUIComponent field) {
		ViewPager tv = (ViewPager)control;
		int val = tv.getCurrentItem();
		IWritable writer = field.getAdaptor(IWritable.class);
		if(writer != null){
			writer.setValue(val);
		}
		return attrType.getValueType().cast(val);
	}

	@Override
	public boolean acceptable(Object control) {
		return control instanceof ViewPager;
	}

}
