package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.stock.client.widget.ImageRefreshListView;
import com.wxxr.mobile.stock.client.widget.ImageRefreshViewKeys;

public class ImageRefreshListViewFieldBinding extends BasicFieldBinding {

	public ImageRefreshListViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}
	
	@Override
	protected void updateUI(boolean arg0) {
		IUIComponent comp = getField();
		String val = comp.getAttribute(ImageRefreshViewKeys.userHomeBackUri);
		//BuyStockDetailInputView view = (BuyStockDetailInputView)getUIControl();
		ImageRefreshListView view = (ImageRefreshListView) getUIControl();
		if(val != null){
			view.setUserHomeBack(val);
		}
		val = comp.getAttribute(ImageRefreshViewKeys.userIconUri);
		if(val != null){
			view.setUserIconBack(val);
		}
		val = comp.getAttribute(ImageRefreshViewKeys.totalMoneyProfit);
		if(val != null){
			view.setTotalMoneyProfit(val);
		}
		val = comp.getAttribute(ImageRefreshViewKeys.totalScoreProfit);
		if(val != null){
			view.setTotalScoreProfit(val);
		}
		super.updateUI(arg0);
	}
	
}
