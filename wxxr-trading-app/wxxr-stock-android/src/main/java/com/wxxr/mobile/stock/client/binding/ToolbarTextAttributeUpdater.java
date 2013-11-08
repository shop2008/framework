/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.stock.client.widget.Rotate3DViewSwitcher;

/**
 * @author duzhen
 *
 */
public class ToolbarTextAttributeUpdater implements IAttributeUpdater<Rotate3DViewSwitcher> {

	@Override
	public <T> void updateControl(Rotate3DViewSwitcher control,
			AttributeKey<T> attrType, IUIComponent field, Object value) {
		String message = (String)field.getAttribute(attrType);
		control.setText(message);
	}

	@Override
	public <T> T updateModel(Rotate3DViewSwitcher control, AttributeKey<T> attrType,
			IUIComponent field) {
		return null;
	}

	@Override
	public boolean acceptable(Object control) {
		return control instanceof Rotate3DViewSwitcher;
	}

}
