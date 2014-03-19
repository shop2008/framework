package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.stock.client.widget.StockSearchView;
import com.wxxr.mobile.stock.client.widget.StockSearchViewKeys;

public class StockSearchViewFieldBinding extends BasicFieldBinding {

	public StockSearchViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}

	@Override
	protected void updateUI(boolean arg0) {
		IUIComponent comp = getField();
		StockSearchView view = (StockSearchView) getUIControl();
		
		Boolean keyVal = (Boolean) comp
				.getAttribute(StockSearchViewKeys.keyBoardViewVisible);
		if (keyVal != null) {
			view.setKeyBoardViewVisible(keyVal.booleanValue());
		}

		Boolean keyShowVal = (Boolean) comp
				.getAttribute(StockSearchViewKeys.keyBoardShow);

		if (keyShowVal != null) {
			if(keyShowVal.booleanValue() == true)
				view.hideSoftKeyBoard();
		}
		super.updateUI(arg0);
	}

}
