package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import android.widget.ListAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.AdapterViewFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.stock.client.widget.MinuteLineViewKeys;
import com.wxxr.mobile.stock.client.widget.PageSwiperView;

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
	protected void updateUI(boolean arg0) {
		IUIComponent comp = getField();
		PageSwiperView view = (PageSwiperView)getUIControl();
		
		Boolean isPagination = comp.getAttribute(MinuteLineViewKeys.isPagination);
		if(isPagination!=null){
			view.setShowPagination(isPagination);
		}
	}
}
