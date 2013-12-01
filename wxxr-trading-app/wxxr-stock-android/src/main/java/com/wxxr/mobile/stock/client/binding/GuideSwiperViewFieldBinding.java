package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import android.widget.ListAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.AdapterViewFieldBinding;
import com.wxxr.mobile.stock.client.widget.GuideSwiperView;

public class GuideSwiperViewFieldBinding extends AdapterViewFieldBinding {

	public GuideSwiperViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}

	@Override
	protected void setupAdapter(ListAdapter adapter) {
		
		((GuideSwiperView)getUIControl()).setAdapter(adapter);
	}
}
