package com.wxxr.mobile.stock.client.binding;

import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIDecorator;
import com.wxxr.mobile.core.ui.api.IUIUpdatingContext;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.stock.client.R;

public class StockViewDecorator implements IUIDecorator {

	private IUIDecorator delegate;

	public StockViewDecorator(IUIDecorator delagate) {
		this.delegate = delagate;
	}

	@Override
	public void handleUIUpdating(IUIUpdatingContext context, IUIComponent comp,
			Object uiControl) {
		View v = null;
		if (comp.hasAttribute(AttributeKeys.valueUpdating)) {
			v = ((ViewGroup) ((View) uiControl).getParent())
					.findViewById(R.id.progress);
			if (v != null)
				v.setVisibility(View.VISIBLE);
			v = ((ViewGroup) ((View) uiControl).getParent())
					.findViewById(R.id.failed);
			if (v != null)
				v.setVisibility(View.GONE);
			return;
		}
		if (comp.hasAttribute(AttributeKeys.valueUpdatedFailed)) {
			v = ((ViewGroup) ((View) uiControl).getParent())
					.findViewById(R.id.progress);
			if (v != null)
				v.setVisibility(View.GONE);
			v = ((ViewGroup) ((View) uiControl).getParent())
					.findViewById(R.id.failed);
			if (v != null)
				v.setVisibility(View.VISIBLE);
			return;
		}
		v = ((ViewGroup) ((View) uiControl).getParent())
				.findViewById(R.id.progress);
		if (v != null)
			v.setVisibility(View.GONE);
		v = ((ViewGroup) ((View) uiControl).getParent())
				.findViewById(R.id.failed);
		if (v != null)
			v.setVisibility(View.GONE);
		this.delegate.handleUIUpdating(context, comp, uiControl);
	}

}
