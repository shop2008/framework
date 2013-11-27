package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.stock.client.widget.BuyStockDetailInputView;
import com.wxxr.mobile.stock.client.widget.BuyStockViewKeys;

public class BuyStockViewFieldBinding extends BasicFieldBinding {

	public BuyStockViewFieldBinding(IAndroidBindingContext ctx,
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

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#updateUI(boolean)
	 */
	@Override
	protected void updateUI(boolean arg0) {
		IUIComponent comp = getField();
		String val = comp.getAttribute(BuyStockViewKeys.marketPrice);
		BuyStockDetailInputView view = (BuyStockDetailInputView)getUIControl();
		if(val != null){
			view.setMarketPrice(val);
		}
		val = comp.getAttribute(BuyStockViewKeys.orderPrice);
		if(val != null){
			view.setOrderPrice(val);
		}
		val = comp.getAttribute(BuyStockViewKeys.fund);
		if(val != null){
			view.setFund(val);
		}
		super.updateUI(arg0);
	}
	
}
