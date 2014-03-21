/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import android.view.View;
import android.widget.EditText;

import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.ValidationError;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

/**
 * @author dz
 * 
 */
public class EditTextAttributeUpdater implements IAttributeUpdater<View> {

	@Override
	public <T> void updateControl(final View control,
			final AttributeKey<T> attrType, final IUIComponent field,
			Object value) {
		if (!(control instanceof EditText)) {
			return;
		}
		EditText edit = (EditText) control;
		ValidationError[] validation = null;
		edit.setError(null);
		if (attrType == AttributeKeys.validationErrors)
			validation = field.getAttribute(AttributeKeys.validationErrors);
		if (validation != null && validation.length > 0) {
			StringBuffer buffer = new StringBuffer();
			int cnt = 0;
			for (ValidationError error : validation) {
				if (cnt > 0)
					buffer.append('\n');
				buffer.append(error.getErrorMessage());
				cnt++;
			}
			edit.setError(buffer.toString());
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
