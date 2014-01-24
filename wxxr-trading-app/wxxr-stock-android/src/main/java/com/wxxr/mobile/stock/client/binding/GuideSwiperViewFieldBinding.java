package com.wxxr.mobile.stock.client.binding;

import java.util.Map;

import android.widget.ListAdapter;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.AdapterViewFieldBinding;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.stock.client.widget.GuideSwiperView;
import com.wxxr.mobile.stock.client.widget.GuideSwiperViewKeys;

public class GuideSwiperViewFieldBinding extends AdapterViewFieldBinding {

	public GuideSwiperViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}

	@Override
	protected void setupAdapter(ListAdapter adapter) {
		
		((GuideSwiperView)getUIControl()).setAdapter(adapter);
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
	
	
	@Override
	protected void updateUI(boolean recursive) {
		
		IUIComponent comp = getField();
		
		Integer position = comp.getAttribute(GuideSwiperViewKeys.position);
		GuideSwiperView view = (GuideSwiperView) getUIControl();
		
		if(position != null) {
			view.setCurrentPage(position);
		}
		super.updateUI(recursive);
	}
}
