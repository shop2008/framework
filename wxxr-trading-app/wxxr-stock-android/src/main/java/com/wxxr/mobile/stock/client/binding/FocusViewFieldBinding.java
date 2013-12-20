package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import android.util.Log;
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
	public void doUpdate() {

		super.doUpdate();
	}

	@Override
	protected void updateUI(boolean recursive) {
		IUIComponent comp = getField();
		Boolean val = comp.getAttribute(FocusAttributeKeys.focusable);
		View view = (View) getUIControl();

		if (val != null) {

			view.setFocusable(true);
			view.setFocusableInTouchMode(true);
			view.requestFocus();

		}
		super.updateUI(recursive);
	}
}
