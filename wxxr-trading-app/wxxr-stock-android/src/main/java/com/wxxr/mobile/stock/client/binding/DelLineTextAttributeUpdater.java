/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import android.view.View;
import android.widget.TextView;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IWritable;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.stock.client.widget.DelLineTextView;
import com.wxxr.mobile.stock.client.widget.MinuteLineViewKeys;

/**
 * @author neillin
 *
 */
public class DelLineTextAttributeUpdater implements IAttributeUpdater<View> {
	private static final Trace log = Trace.register(DelLineTextAttributeUpdater.class);
	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field,Object value) {
		if(!(control instanceof DelLineTextView)){
			return;
		}
		DelLineTextView tv = (DelLineTextView)control;
		if(attrType == MinuteLineViewKeys.isDelLine){
			Boolean flag = (Boolean) field.getAttribute(attrType);
			if(flag!=null)
			tv.setIsDelLine(flag);
		}
	}

	@Override
	public <T> T updateModel(View control, AttributeKey<T> attrType,
			IUIComponent field) {
		DelLineTextView tv = (DelLineTextView)control;
		String val = tv.getText().toString();
		IWritable writer = field.getAdaptor(IWritable.class);
		if(writer != null){
			writer.setValue(val);
		}
		return attrType.getValueType().cast(val);
	}

	@Override
	public boolean acceptable(Object control) {
		return control instanceof TextView;
	}

}
