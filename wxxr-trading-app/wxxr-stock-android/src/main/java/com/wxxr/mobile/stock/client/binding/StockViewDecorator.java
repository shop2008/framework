package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import android.view.View;
import android.view.ViewGroup;

import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IUIDecorator;
import com.wxxr.mobile.core.ui.api.IUIUpdatingContext;
import com.wxxr.mobile.core.ui.common.AttributeKeys;

public class StockViewDecorator implements IUIDecorator {

	private IUIDecorator delegate;
	final private static String UPDATINGVIEWID = "updatingViewId";
	final private static String UPDATEFAIELDID = "updateFaieldId";
	final private static String UPDATEALLID = "updateAllId";

	public StockViewDecorator(IUIDecorator delagate) {
		this.delegate = delagate;
	}

	@Override
	public void handleUIUpdating(IUIUpdatingContext context, IUIComponent comp,
			Object uiControl) {
		View all = getUpdateViewById(context, UPDATEALLID, uiControl);
		View updating = getUpdateViewById(context, UPDATINGVIEWID, uiControl);
		View updateFaield = getUpdateViewById(context, UPDATEFAIELDID,
				uiControl);
		if (all != null) {
			all.setVisibility(View.VISIBLE);
			all.setOnClickListener(null);
		}
		if (comp.hasAttribute(AttributeKeys.valueUpdating)) {
			if (updating != null)
				updating.setVisibility(View.VISIBLE);
			if (updateFaield != null)
				updateFaield.setVisibility(View.GONE);
			return;
		}
		if (comp.hasAttribute(AttributeKeys.valueUpdatedFailed)) {
			if (updating != null)
				updating.setVisibility(View.GONE);
			if (updateFaield != null)
				updateFaield.setVisibility(View.VISIBLE);
			return;
		}
		if (updating != null)
			updating.setVisibility(View.GONE);
		if (updateFaield != null)
			updateFaield.setVisibility(View.GONE);
		if (all != null)
			all.setVisibility(View.GONE);
		this.delegate.handleUIUpdating(context, comp, uiControl);
	}

	private View getUpdateViewById(IUIUpdatingContext context, String key,
			Object uiControl) {
		if (context != null) {
			Map<String, String> attrs = context.getBindingContext()
					.getBindingAttrSet();
			String val = attrs.get(key);
			if (val != null) {
				int id = RUtils.getInstance().getResourceIdByURI(val);
				ViewGroup group = ((ViewGroup) ((View) uiControl).getParent());
				View view = null;
				while (group != null && (view = group.findViewById(id)) == null) {
					group = (ViewGroup) group.getParent();
				}
				return view;
			}
		}
		return null;
	}
}
