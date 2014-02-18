package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import android.widget.ListAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.AdapterViewFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.stock.client.widget.ImageRefreshListView;
import com.wxxr.mobile.stock.client.widget.ImageRefreshViewKeys;
import com.wxxr.mobile.stock.client.widget.PageSwiperView;
import com.wxxr.mobile.stock.client.widget.PageSwiperViewKeys;

public class PageSwiperViewFieldBinding extends AdapterViewFieldBinding {

	public PageSwiperViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
		
	}
	@Override
	protected void setupAdapter(ListAdapter adapter) {
		((PageSwiperView)getUIControl()).setAdapter(adapter);
	}
	
	@Override
	protected void updateUI(boolean recursive) {
		IUIComponent comp = getField();
		Boolean val = comp.getAttribute(PageSwiperViewKeys.titleBarVisible);
		PageSwiperView view = (PageSwiperView) getUIControl();
		if(val != null){
			view.setShowPagination(val.booleanValue());
		}
		super.updateUI(recursive);
	}
}
