package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.AdapterViewFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.stock.client.widget.Pull2RefreshViewKeys;
import com.wxxr.mobile.stock.client.widget.PullToRefreshListView;

public class RefreshListViewAdapterBinding extends AdapterViewFieldBinding {

	public RefreshListViewAdapterBinding(IAndroidBindingContext ctx,
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
		Boolean val = comp.getAttribute(Pull2RefreshViewKeys.enablePullDownRefresh);
		PullToRefreshListView view = (PullToRefreshListView)getUIControl();
		if(val != null){
			view.setPullRefreshEnable(val);
		}
		val = comp.getAttribute(Pull2RefreshViewKeys.enablePullUpRefresh);
		if(val != null){
			view.setPullLoadEnable(val);
		}
		super.updateUI(arg0);
	}
	
}
