package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import android.view.View;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.stock.client.widget.FocusAttributeKeys;

public class FocusViewFieldBinding extends BasicFieldBinding {

	public FocusViewFieldBinding(IAndroidBindingContext ctx, String fieldName,
			Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}

	@Override
	protected void updateUI(boolean recursive) {
		IUIComponent comp = getField();
		Boolean val = comp.getAttribute(FocusAttributeKeys.focusable);
		View view = (View) getUIControl();
		if (val != null) {
			
			if (val == true) {
				view.setFocusable(true);
				view.setFocusableInTouchMode(true);
			} else {
				view.clearFocus();
				view.setFocusableInTouchMode(true);
			}
		}
		super.updateUI(recursive);
	}
}
