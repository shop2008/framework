package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.stock.client.widget.BuyStockViewKeys;
import com.wxxr.mobile.stock.client.widget.ViewPagerIndexGroup;

public class ViewPagerIndexGroupFieldBinding extends BasicFieldBinding {

	public ViewPagerIndexGroupFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);

	}

	@Override
	public void activate(IView model) {
		super.activate(model);
	}

	@Override
	public void deactivate() {
		super.deactivate();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void updateModel() {
		super.updateModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wxxr.mobile.android.ui.binding.BasicFieldBinding#updateUI(boolean)
	 */
	@Override
	protected void updateUI(boolean arg0) {
		IUIComponent comp = getField();
		int val = comp.getAttribute(BuyStockViewKeys.size);
		ViewPagerIndexGroup view = (ViewPagerIndexGroup) getUIControl();
		view.setSize(val);
		val = comp.getAttribute(BuyStockViewKeys.position);
		view.setPosition(val);
		super.updateUI(arg0);
	}

}
