package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import android.widget.EditText;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.stock.client.widget.FocusAttributeKeys;

public class EditTextFieldBinding extends BasicFieldBinding {

	public EditTextFieldBinding(IAndroidBindingContext ctx, String fieldName,
			Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}

	@Override
	protected void updateUI(boolean recursive) {
		IUIComponent comp = getField();
		Boolean val = comp.getAttribute(FocusAttributeKeys.focusable);
		EditText view = (EditText) getUIControl();
		if (val != null) {
			// view.setFocusable(val);

			if (val == true) {
				//view.requestFocus();
				view.setFocusableInTouchMode(true);
			} else {
				view.clearFocus();
				view.setFocusableInTouchMode(true);
			}
		}
		super.updateUI(recursive);
	}
}
