package com.wxxr.mobile.stock.client.widget;

import java.util.Map;

import android.widget.AbsListView;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.android.ui.binding.BasicFieldBinding;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;

public class ImageSwiperAdapterViewFieldBinding extends BasicFieldBinding {

	public static final String LIST_ITEM_VIEW_ID = "imageItemViewId";
	private ImageSwiperAdapter imgAdapter;
	public ImageSwiperAdapterViewFieldBinding(IAndroidBindingContext ctx,
			String fieldName, Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
		
	}

	@Override
	public void activate(IView model) {
		super.activate(model);
		String imageItemViewId = getBindingAttrs().get(LIST_ITEM_VIEW_ID);
		IListDataProvider provider = model.getChild(getFieldName()).getAdaptor(IListDataProvider.class);
		this.imgAdapter = new ImageSwiperAdapter(getWorkbenchContext(), getAndroidBindingContext().getUIContext(), provider, imageItemViewId);
		((AbsListView)getUIControl()).setAdapter(imgAdapter);
	}
	@Override
	public void deactivate() {
		if(this.imgAdapter != null){
			((AbsListView)getUIControl()).setAdapter(null);
//			this.imgAdapter.destroy();
			this.imgAdapter = null;
		}
		super.deactivate();
	}	
}
