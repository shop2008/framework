/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Map;

import android.widget.AbsListView;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;

/**
 * @author neillin
 *
 */
public class AdapterViewFieldBinding extends BasicFieldBinding {
	public static final String LIST_ITEM_VIEW_ID = "itemViewId";
	private GenericListAdapter listAdapter;
	
	public AdapterViewFieldBinding(IAndroidBindingContext ctx, String fieldName,
			Map<String, String> attrSet) {
		super(ctx, fieldName, attrSet);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#activate(com.wxxr.mobile.core.ui.api.IUIComponent)
	 */
	@Override
	public void activate(IView model) {
		super.activate(model);
		String itemViewId = getBindingAttrs().get(LIST_ITEM_VIEW_ID);
		IListDataProvider provider = model.getAdaptor(IListDataProvider.class);
		this.listAdapter = new GenericListAdapter(getWorkbenchContext(), getAndroidBindingContext().getUIContext(), provider, itemViewId);		
		((AbsListView)getUIControl()).setAdapter(listAdapter);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.binding.BasicFieldBinding#deactivate()
	 */
	@Override
	public void deactivate() {
		if(this.listAdapter != null){
			((AbsListView)getUIControl()).setAdapter(null);
			this.listAdapter.destroy();
			this.listAdapter = null;
		}
		super.deactivate();
	}

}
