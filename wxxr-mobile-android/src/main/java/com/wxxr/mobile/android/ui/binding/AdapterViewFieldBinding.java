/**
 * 
 */
package com.wxxr.mobile.android.ui.binding;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.widget.AbsListView;

import com.wxxr.mobile.android.ui.IAndroidBindingContext;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IListDataProvider;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.DataField;
import com.wxxr.mobile.core.ui.common.UIComponent;

/**
 * @author neillin
 *
 */
public class AdapterViewFieldBinding extends BasicFieldBinding {
	public static final String LIST_ITEM_VIEW_ID = "itemViewId";
	public static final String LIST_FOOTER_VIEW_ID = "footerViewId";
	public static final String LIST_HEADER_VIEW_ID = "headererViewId";
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
		String footerViewId = getBindingAttrs().get(LIST_FOOTER_VIEW_ID);
		String headerViewId = getBindingAttrs().get(LIST_HEADER_VIEW_ID);
		IUIComponent comp = model.getChild(getFieldName());
		IListDataProvider provider = comp.getAdaptor(IListDataProvider.class);
		if(provider == null){
			if(comp instanceof IDataField){
				Object val = ((IDataField<?>)comp).getValue();
				provider = createAdaptorFromValue(val);
			}
		}
		if(provider == null){
			provider = createAdaptorFromValue(comp.getAttribute(AttributeKeys.options));
		}
		this.listAdapter = new GenericListAdapter(getWorkbenchContext(), getAndroidBindingContext().getUIContext(), provider, itemViewId,headerViewId,footerViewId);		
		((AbsListView)getUIControl()).setAdapter(listAdapter);
	}

	/**
	 * @param provider
	 * @param val
	 * @return
	 */
	protected IListDataProvider createAdaptorFromValue(Object val) {
		IListDataProvider provider = null;
		if(val instanceof List){
			final List data = (List)val;
			provider = new IListDataProvider() {
				
				@Override
				public Object getItemId(Object item) {
					return null;
				}
				
				@Override
				public int getItemCounts() {
					return data.size();
				}
				
				@Override
				public Object getItem(int i) {
					return data.get(i);
				}
			};
		}else if((val != null)&& val.getClass().isArray()){
			final Object[] data = (Object[])val;
			provider = new IListDataProvider() {
				
				@Override
				public Object getItemId(Object item) {
					return null;
				}
				
				@Override
				public int getItemCounts() {
					return data.length;
				}
				
				@Override
				public Object getItem(int i) {
					return data[i];
				}
			};
		}
		return provider;
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
